package controllers;

import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Класс, добавляющий функциональность для сохранения и загрузки задач в файл.
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;  //переменная, сохраняющая файл для сохранения задач.

    //конструктор, принимающий и сохраняющий файл для сохранения данных
    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int addTask(Task task) {  //метод добавляет задачу и сохраняет изменения в файл
        int taskId = super.addTask(task);
        save();
        return taskId;
    }

    @Override
    public int addTask(SubTask subTask) {  //метод добавляет подзадачу и сохраняет изменения в файл
        int taskId = super.addTask(subTask);
        save();
        return taskId;
    }

    @Override
    public int addTask(Epic epic) {  //метод добавляет эпик и сохраняет изменения в файл
        int taskId = super.addTask(epic);
        save();
        return taskId;
    }

    @Override
    public Epic addEpic(Epic epic) {  //метод добавляет эпик и сохраняет изменения в файл
        Epic addedEpic = super.addEpic(epic);
        save();
        return addedEpic;
    }

    @Override
    public Task updateTask(Task task) {  //метод обновляет задачу и сохраняет изменения в файл
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public void updateEpic(Epic epic) {  //метод обновляет эпик и сохраняет изменения в файл
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {  //метод обновляет подзадачу и сохраняет изменения в файл
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTasks() {  //метод удаляет все задачи и сохраняет изменения в файл
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() { //метод удаляет все эпики и сохраняет изменения в файл
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() { //метод удаляет все подзадачи и сохраняет изменения в файл
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteTaskByID(int id) {  //метод удаляет задачу по id и сохраняет изменения в файл
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) {  //метод удаляет эпик по id и сохраняет изменения в файл
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {  //метод удаляет подзадачу по id и сохраняет изменения в файл
        super.deleteSubtaskByID(id);
        save();
    }

    // Метод для загрузки менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) { // Пропускаем заголовок
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task task = taskFromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    manager.addTask((SubTask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке из файла", e);
        }
        return manager;
    }

    //метод сохранения задач в файл
    private void save() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("id,type,name,status,description,duration,startTime,endTime,epic");
            for (Task task : getTasks()) {
                lines.add(taskToString(task));
            }
            for (Epic epic : getEpics()) {
                lines.add(taskToString(epic));
            }
            for (SubTask subtask : getSubtasks()) {
                lines.add(taskToString(subtask));
            }
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }

    //метод преобразования задачи в строку для записи в файл
    private String taskToString(Task task) {
        String type = task.getType().toString();
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "0";
        String startTime = task.getStartTime() != null ? task.getStartTime().toString() : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().toString() : "";

        if (task instanceof SubTask) {
            String epicId = String.valueOf(((SubTask) task).getEpicID());
            return String.join(",",
                    String.valueOf(task.getId()),
                    type,
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    duration,
                    startTime,
                    endTime,
                    epicId
            );
        } else {
            return String.join(",",
                    String.valueOf(task.getId()),
                    type,
                    task.getName(),
                    task.getStatus().toString(),
                    task.getDescription(),
                    duration,
                    startTime,
                    endTime
            );
        }
    }

    //метод создает задачи из строки при загрузке из файла
    private static Task taskFromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 8) {
            throw new IllegalArgumentException("Некорректная строка задачи: " + value);
        }
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(parts[5]));
        LocalDateTime startTime = parts[6].isEmpty() ? null : LocalDateTime.parse(parts[6]);
        LocalDateTime endTime = parts[7].isEmpty() ? null : LocalDateTime.parse(parts[7]);

        if (type == TaskType.SUBTASK) {
            if (parts.length < 9) {
                throw new IllegalArgumentException("Некорректная строка подзадачи: " + value);
            }
            int epicId = Integer.parseInt(parts[8]);
            return new SubTask(name, description, id, status, duration, startTime, epicId);
        } else if (type == TaskType.EPIC) {
            return new Epic(name, description, id, status, duration, startTime, endTime, new ArrayList<>());
        } else {
            return new Task(name, description, id, status, TaskType.TASK, duration, startTime);
        }
    }
}
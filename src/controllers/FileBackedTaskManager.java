package controllers;

import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

//Класс, добавляющий функциональность для сохранения и загрузки задач в файл.
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file; //переменная, сохраняющая файл для сохранения задач.

    //конструктор, принимающий и сохраняющий файл для сохранения данных
    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int addTask(Task task) { //метод добавляет задачу и сохраняет изменения в файл
        int taskId = super.addTask(task);
        save();
        return taskId;
    }

    @Override
    public int addTask(SubTask subTask) { //метод добавляет подзадачу и сохраняет изменения в файл
        int taskId = super.addTask(subTask);
        save();
        return taskId;
    }

    @Override
    public int addTask(Epic epic) { //метод добавляет эпик и сохраняет изменения в файл
        int taskId = super.addTask(epic);
        save();
        return taskId;
    }

    @Override
    public Epic addEpic(Epic epic) { //метод добавляет эпик и сохраняет изменения в файл
        Epic addedEpic = super.addEpic(epic);
        save();
        return addedEpic;
    }


    @Override
    public Task updateTask(Task task) { //метод обновляет задачу и сохраняет изменения в файл
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public void updateEpic(Epic epic) { //метод обновляет эпик и сохраняет изменения в файл
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) { //метод обновляет подзадачу и сохраняет изменения в файл
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTasks() { //метод удаляет все задачи и сохраняет изменения в файл
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
    public void deleteTaskByID(int id) { //метод удаляет задачу по id и сохраняет изменения в файл
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) { //метод удаляет эпик по id и сохраняет изменения в файл
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) { //метод удаляет подзадачу по id и сохраняет изменения в файл
        super.deleteSubtaskByID(id);
        save();
    }

    // Метод для загрузки менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                return manager;
            }

            // Пропускаем заголовок
            for (int i = 1; i < lines.size(); i++) {
                Task task = taskFromString(lines.get(i));
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    manager.addTask((SubTask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
        return manager;
    }

    private void save() { //метод сохраняет текущее состояние менеджера задач в файл. Данные хранятся в формате CSV
        try {
            List<String> lines = new ArrayList<>();
            lines.add("id,type,name,status,description,epic"); // Заголовок CSV

            // Сохраняем задачи
            for (Task task : getTasks()) {
                lines.add(taskToString(task));
            }
            for (Epic epic : getEpics()) {
                lines.add(taskToString(epic));
            }
            for (SubTask subtask : getSubtasks()) {
                lines.add(taskToString(subtask));
            }

            // Записываем в файл
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }

    //Метод преобразует задачу в CSV
    private String taskToString(Task task) {
        String type = task.getType().toString();
        String epicId = (task instanceof SubTask) ? String.valueOf(((SubTask) task).getEpicID()) : "";
        return String.join(",",
                String.valueOf(task.getId()),
                type,
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                epicId
        );
    }

    // Метод для создания задачи из строки CSV
    private static Task taskFromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        int epicId = parts.length > 5 && !parts[5].isEmpty() ? Integer.parseInt(parts[5]) : -1;

        switch (type) {
            case TASK:
                return new Task(name, description, id, status, TaskType.TASK);
            case EPIC:
                return new Epic(name, description, id, status, new ArrayList<>());
            case SUBTASK:
                return new SubTask(name, description, id, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}
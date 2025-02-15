package controllers;

import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод для сохранения состояния менеджера в файл


    // Метод для преобразования задачи в строку CSV


    // Переопределение методов для автосохранения
    @Override
    public int addTask(Task task) {
        int taskId = super.addTask(task);
        save();
        return taskId;
    }

    @Override
    public int addTask(SubTask subTask) {
        int taskId = super.addTask(subTask);
        save();
        return taskId;
    }

    @Override
    public int addTask(Epic epic) {
        int taskId = super.addTask(epic);
        save();
        return taskId;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic addedEpic = super.addEpic(epic);
        save();
        return addedEpic;
    }


    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) {
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {
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

    private String taskToString(Task task) {
        String type;
        if (task instanceof Epic) {
            type = "EPIC";
        } else if (task instanceof SubTask) {
            type = "SUBTASK";
        } else {
            type = "TASK";
        }

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
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        int epicId = parts.length > 5 && !parts[5].isEmpty() ? Integer.parseInt(parts[5]) : -1;

        switch (type) {
            case "TASK":
                return new Task(name, description, id, status);
            case "EPIC":
                return new Epic(name, description, id, status, new ArrayList<>());
            case "SUBTASK":
                return new SubTask(name, description, id, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public void save() {
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
}
package controllers;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>(); // Хранилище задач
    private final Map<Integer, Epic> epics = new HashMap<>(); // Хранилище эпиков
    private final Map<Integer, SubTask> subtasks = new HashMap<>(); // Хранилище подзадач
    private final HistoryManager historyManager = Manager.getDefaultHistory(); // Менеджер истории
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime)); // Приоритетный список задач
    private int id = 1;

    //метод добавления задачи в приоритетный список с проверкой на пересечение
    @Override
    public int addTask(Task task) {
        if (task.getStartTime() != null && hasOverlap(task)) { //проверка на пересечение
            throw new IllegalArgumentException("Задача пересекается с существующей задачей");
        }
        int taskId = getID();
        task.setId(taskId);
        tasks.put(taskId, task);
        updatePrioritizedTasks(task); //добавление задачи в приоритетный список
        return taskId;
    }

    //метод добавления подзадачи в приоритетный список с проверкой на пересечение
    @Override
    public int addTask(SubTask subTask) {
        if (subTask.getStartTime() != null && hasOverlap(subTask)) { //проверка на пересечение
            throw new IllegalArgumentException("Подзадача пересекается с существующей подзадачей");
        }
        int taskId = getID();
        subTask.setId(taskId);
        subtasks.put(taskId, subTask);
        if (findEpic(subTask.getEpicID())) {
            Epic epic = epics.get(subTask.getEpicID());
            epic.addSubtasksId(taskId);
            updateEpicFields(epic);
        }
        updatePrioritizedTasks(subTask); //добавление задачи в приоритетный список
        return taskId;
    }

    //метод добавления эпика
    @Override
    public int addTask(Epic epic) {
        int taskId = getID();
        epic.setId(taskId);
        epics.put(taskId, epic);
        updateEpicFields(epic);
        return taskId;
    }

    //альтернативный метод добавления эпика с возвратом объекта
    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getID());
        epics.put(epic.getId(), epic);
        updateEpicFields(epic);
        return epic;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskID = task.getId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            throw new NotFoundException("Задача с ID " + taskID + " не найдена для обновления");
        }
        Task oldTask = tasks.get(taskID);
        if (oldTask != null) {
            prioritizedTasks.remove(oldTask);
        }
        if (task.getStartTime() != null && hasOverlap(task)) {
            if (oldTask != null) {
                prioritizedTasks.add(oldTask);
            }
            throw new IllegalArgumentException("Обновляемая задача пересекается с существующей задачей");
        }
        tasks.put(taskID, task);
        updatePrioritizedTasks(task);
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic existingEpic = epics.get(epic.getId());
        if (existingEpic == null) {
            throw new NotFoundException("Эпик с ID " + epic.getId() + " не найден для обновления");
        }
        existingEpic.setName(epic.getName());
        existingEpic.setDescription(epic.getDescription());
        updateEpicFields(existingEpic);
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        SubTask existingSubTask = subtasks.get(subtask.getId());
        if (existingSubTask != null) {
            String oldName = existingSubTask.getName();
            String oldDescription = existingSubTask.getDescription();
            Status oldStatus = existingSubTask.getStatus();
            Duration oldDuration = existingSubTask.getDuration();
            LocalDateTime oldStartTime = existingSubTask.getStartTime();

            existingSubTask.setName(subtask.getName());
            existingSubTask.setDescription(subtask.getDescription());
            existingSubTask.setStatus(subtask.getStatus());
            existingSubTask.setDuration(subtask.getDuration());
            existingSubTask.setStartTime(subtask.getStartTime());

            if (subtask.getStartTime() != null && hasOverlap(existingSubTask)) {
                existingSubTask.setName(oldName);
                existingSubTask.setDescription(oldDescription);
                existingSubTask.setStatus(oldStatus);
                existingSubTask.setDuration(oldDuration);
                existingSubTask.setStartTime(oldStartTime);
                throw new IllegalArgumentException("Обновляемая подзадача пересекается с существующей задачей");
            }

            updatePrioritizedTasks(existingSubTask);
            if (findEpic(existingSubTask.getEpicID())) {
                updateEpicFields(epics.get(existingSubTask.getEpicID()));
            }
        } else {
            throw new NotFoundException("Подзадача с ID " + subtask.getId() + " не найдена для обновления");
        }
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с ID " + id + " не найдена");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпик с ID " + id + " не найден");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubtaskByID(int id) {
        SubTask subTask = subtasks.get(id);
        if (subTask == null) {
            throw new NotFoundException("Подзадача с ID " + id + " не найдена");
        }
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<SubTask> getEpicSubtasks(Epic epic) {
        List<Integer> subTasksId = epic.getSubTasksId();
        if (subTasksId == null || subTasksId.isEmpty()) {
            return new ArrayList<>(); // Возвращаем пустой список, если подзадач нет
        }
        return subTasksId.stream()
                .map(subtasks::get)
                .filter(Objects::nonNull) // Фильтруем null значения
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (SubTask subTask : subtasks.values()) {
            prioritizedTasks.remove(subTask);
            historyManager.remove(subTask.getId());
        }
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (SubTask subTask : subtasks.values()) {
            prioritizedTasks.remove(subTask);
            historyManager.remove(subTask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicFields(epic);
        }
    }

    @Override
    public void deleteTaskByID(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicByID(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (SubTask subTask : getEpicSubtasks(epic)) {
                prioritizedTasks.remove(subTask);
                subtasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskByID(int id) {
        SubTask subTask = subtasks.remove(id);
        if (subTask != null) {
            prioritizedTasks.remove(subTask);
            Epic epic = epics.get(subTask.getEpicID());
            if (epic != null) {
                epic.getSubTasksId().remove(Integer.valueOf(id));
                updateEpicFields(epic);
            }
            historyManager.remove(id);
        }
    }

    // Получение приоритетного списка задач
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean findEpic(int id) {
        return epics.containsKey(id);
    }

    private int getID() {
        return id++;
    }

    // Обновление полей эпика на основе подзадач
    private void updateEpicFields(Epic epic) {
        List<SubTask> list = getEpicSubtasks(epic);
        if (list == null || list.isEmpty()) {
            epic.setStatus(Status.NEW);
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        boolean allNew = list.stream().allMatch(subtask -> subtask.getStatus() == Status.NEW);
        boolean allDone = list.stream().allMatch(subtask -> subtask.getStatus() == Status.DONE);
        boolean hasInProgress = list.stream().anyMatch(subtask -> subtask.getStatus() == Status.IN_PROGRESS);

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

        LocalDateTime earliestStart = list.stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        Duration totalDuration = list.stream()
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        LocalDateTime latestEnd = list.stream()
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        epic.setStartTime(earliestStart);
        epic.setDuration(totalDuration);
        epic.setEndTime(latestEnd);
    }

    //обновление приоритетного списка задач
    private void updatePrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        } else {
            prioritizedTasks.remove(task);
        }
    }

    //проверка пересечений задач по времени
    private boolean hasOverlap(Task newTask) {
        LocalDateTime start = newTask.getStartTime();
        LocalDateTime end = newTask.getEndTime();
        if (start == null || end == null) {
            return false;
        }
        return prioritizedTasks.stream()
                .filter(task -> task != newTask)
                .anyMatch(task -> task.getStartTime() != null && task.getEndTime() != null &&
                        start.isBefore(task.getEndTime()) && task.getStartTime().isBefore(end));
    }
}
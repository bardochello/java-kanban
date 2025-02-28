package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();
    private LocalDateTime endTime; // Время завершения эпика

    //Конструкторы
    public Epic(String name, String description) {
        super(name, description, TaskType.EPIC, Duration.ZERO, null);
        this.subTasksId = new ArrayList<>(); // Инициализация пустым списком
        this.endTime = null;
    }

    public Epic(String name, String description, int id, Status status, Duration duration,
                LocalDateTime startTime, LocalDateTime endTime, List<Integer> subTasksId) {
        super(name, description, id, status, TaskType.EPIC, duration, startTime);
        this.endTime = endTime;
        this.subTasksId = (subTasksId != null) ? subTasksId : new ArrayList<>(); // Защита от null
    }

    //методы для работы с подзадачами
    public void addSubtasksId(int subTask) {
        subTasksId.add(subTask);
    }

    public void clearSubtasks() {
        subTasksId.clear();
    }

    //Геттеры и сеттеры
    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = (subTasksId != null) ? subTasksId : new ArrayList<>();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    //Переопределение метода toString объекта
    @Override
    public String toString() {
        return "Epic{" +
                "name:'" + getName() + '\'' +
                ", description:'" + getDescription() + '\'' +
                ", id:'" + getId() +
                ", subtaskList.size:'" + subTasksId.size() +
                ", status:" + getStatus() +
                ", duration:" + (getDuration() != null ? getDuration().toMinutes() : "null") +
                ", startTime:" + getStartTime() +
                ", endTime:" + endTime +
                "}";
    }
}
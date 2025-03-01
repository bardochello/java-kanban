package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicID; // ID эпика, к которому относится подзадача

    //Конструкторы
    public SubTask(String name, String description, int epicID, Duration duration, LocalDateTime startTime) {
        super(name, description, TaskType.SUBTASK, duration, startTime);
        this.epicID = epicID;
    }

    public SubTask(String name, String description, int id, Status status, Duration duration, LocalDateTime startTime, int epicID) {
        super(name, description, id, status, TaskType.SUBTASK, duration, startTime);
        this.epicID = epicID;
    }

    //Геттеры и сеттеры
    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    //Переопределение метода toString объекта
    @Override
    public String toString() {
        return "SubTask{" +
                "name:'" + getName() + '\'' +
                ", description:'" + getDescription() + '\'' +
                ", id:" + getId() +
                ", epicID:" + epicID +
                ", status:" + getStatus() +
                ", duration:" + (getDuration() != null ? getDuration().toMinutes() : "null") +
                ", startTime:" + getStartTime() +
                ", endTime:" + getEndTime() +
                "}";
    }
}
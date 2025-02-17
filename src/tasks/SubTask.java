package tasks;

public class SubTask extends Task {

    private int epicID;

    public SubTask(String name, String description, int epicID) {
        super(name, description, TaskType.SUBTASK); // Указываем тип задачи
        this.epicID = epicID;
    }

    public SubTask(String name, String description, int id, Status status, int epicID) {
        super(name, description, id, status, TaskType.SUBTASK); // Указываем тип задачи
        this.epicID = epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name: \'" + getName() + '\'' +
                ", description: \'" + getDescription() + '\'' +
                ", id:" + getId() +
                ", epicID: " + epicID +
                ", status: " + getStatus() +
                " }";
    }
}
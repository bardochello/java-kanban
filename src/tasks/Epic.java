package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description, TaskType type) {
        super(name, description, TaskType.EPIC); // Указываем тип задачи
    }

    public Epic(String name, String description, int id, Status status, ArrayList<Integer> subTasksId) {
        super(name, description, id, status, TaskType.EPIC); // Указываем тип задачи
        this.subTasksId = subTasksId;
    }

    public void addSubtasksId(int subTask) {
        subTasksId.add(subTask);
    }

    public void clearSubtasks() {
        subTasksId.clear();
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }

    @Override
    public String toString() {
        return "data.Epic{" +
                "name: \'" + getName() + '\'' +
                ", description: \'" + getDescription() + '\'' +
                ", id: \'" + getId() +
                ", subtaskList.size: \'" + subTasksId.size() +
                ", status: " + getStatus() +
                " },";
    }
}
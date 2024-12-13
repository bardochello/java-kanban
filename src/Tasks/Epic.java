package Tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id, Status status, ArrayList<Integer> subTasksId) {
        super(name, description, id, status);
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
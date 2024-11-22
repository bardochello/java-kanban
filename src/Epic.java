import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public void addSubtask(SubTask subtask) {
        subtaskList.add(subtask);
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public ArrayList<SubTask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(ArrayList<SubTask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name: \'" + getName() + '\'' +
                ", description: \'" + getDescription() + '\'' +
                ", id: \'" + getId() +
                ", subtaskList.size: \'" + subtaskList.size() +
                ", status: " + getStatus() +
                " },";
    }
}
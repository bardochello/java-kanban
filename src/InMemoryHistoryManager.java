import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private static final int MAX_SIZE = 10;
    private ArrayList<Task> taskHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (taskHistory.size() == MAX_SIZE) {
            taskHistory.removeFirst();
        }

        taskHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(taskHistory);
    }
}

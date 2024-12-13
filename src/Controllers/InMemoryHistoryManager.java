package Controllers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_SIZE = 10;
    List<Task> taskHistory = new ArrayList<>();

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
    public List<Task> getHistory() {
        return taskHistory;
    }
}

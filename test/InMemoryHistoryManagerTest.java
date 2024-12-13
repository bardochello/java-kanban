import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task;
    private final int MAX_SIZE = 10;

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefaultHistory();
        task = new Task("task1", "task1 description");
    }

    @Test
    public void add() {
        historyManager.add(task);
        ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(1, history.size(), "History is empty");
    }

    @Test
    public void checkMaxSizeHistory() {
        ArrayList<Task> history;

        history = historyManager.getHistory();
        assertEquals(0, history.size(), "History is not empty");

        for (int i = 1; i <= MAX_SIZE; i++) {
            history.add(task);
        }

        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(MAX_SIZE, history.size(), "History is not full");
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(MAX_SIZE, history.size(), "History is not full");
    }

    @Test
    void checkEmptyHistory() {
        final ArrayList<Task> emptyArray = new ArrayList<>();
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(history, emptyArray, "History is not empty");
        assertEquals(0, history.size(), "History is not empty");
    }
}
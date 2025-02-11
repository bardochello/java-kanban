import Controllers.HistoryManager;
import Controllers.Manager;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task;
    Task task1;
    Task task2;
    Task task3;
    private final int MAX_SIZE = 10;

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefaultHistory();
        task = new Task("task1", "task1 description");
    }

    @Test
    public void add() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(1, history.size(), "History is empty");
    }

    @Test
    public void checkMaxSizeHistory() {
        List<Task> history;

        history = historyManager.getHistory();
        assertEquals(0, history.size(), "History is not empty");

        for (int i = 1; i <= MAX_SIZE; i++) {
            task = new Task("task" + i, "task" + i);
            task.setId(i);
            historyManager.add(task);
        }

        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(MAX_SIZE, history.size(), "History size was changed");

        task = new Task("task", "task");
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        task.setId(MAX_SIZE + 1);
        assertTrue(MAX_SIZE < history.size(), "History size smaller than old limit");
    }

    @Test
    void checkEmptyHistory() {
        final ArrayList<Task> emptyArray = new ArrayList<>();
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(history, emptyArray, "History is not empty");
        assertEquals(0, history.size(), "History is not empty");
    }

    @Test
    void checkAddOneTaskTwice() { //проверяем что одна и та же задача не добавляется дважды
        ArrayList<Task> history;
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(1, history.size(), "История не равна 1");
        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(1, history.size(), "История не равна 1");
    }

    @Test
    void checkDeleteLastTask() { //проверяем удаление из истории
        ArrayList<Task> history;
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(List.of(task1, task2,task3), history, "История не верна");
        historyManager.remove(task3.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(List.of(task1,task2), history, "История не верна");
    }

    @Test
    void checkGetTaskListOrder() { //проверяем порядок элементов в истории
        ArrayList<Task> history;
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(List.of(task1), history, "История не верна");
        historyManager.add(task2);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(List.of(task1, task2), history, "История не верна");
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "История null.");
        assertEquals(List.of(task2, task1), history, "История не верна");
    }
}


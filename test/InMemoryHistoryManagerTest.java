import tasks.Task;
import controllers.HistoryManager;
import controllers.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    private final int MAX_SIZE = 10;

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefaultHistory();
        task1 = new Task("task1", "task description", TaskType.TASK);
        task1.setId(0);
        task2 = new Task("task2", "task2 description", TaskType.TASK);
        task2.setId(1);
        task3 = new Task("task3", "task3 description", TaskType.TASK);
        task3.setId(2);
        task4 = new Task("task4", "task4 description", TaskType.TASK);
        task4.setId(3);
    }

    @Test
    public void add() {
        historyManager.add(task1);
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
            task1 = new Task("task" + i, "task" + i, TaskType.TASK);
            task1.setId(i);
            historyManager.add(task1);
        }

        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(MAX_SIZE, history.size(), "History size was changed");

        task1 = new Task("task", "task", TaskType.TASK);
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        task1.setId(MAX_SIZE + 1);
        assertTrue(MAX_SIZE < history.size(), "History size smaller than old limit");
    }

    @Test
    void checkEmptyHistory() {
        final List<Task> emptyArray = new ArrayList<>();
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(history, emptyArray, "History is not empty");
        assertEquals(0, history.size(), "History is not empty");
    }

    @Test
    void checkAddOneTaskTwice() {
        List<Task> history;
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "History is not empty.");
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(1, history.size(), "History size is not 1");
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(1, history.size(), "History size is not 1");
    }

    @Test
    void checkDeleteLastTask() { //проверяем удаление из истории
        List<Task> history;
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "History is incorrect");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(List.of(task1, task2,task3), history, "History is incorrect");
        historyManager.remove(task3.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(List.of(task1,task2), history, "History is incorrect");
    }

    @Test
    void checkGetTaskListOrder() {
        List<Task> history;


        history = historyManager.getHistory();
        assertEquals(0, history.size(), "History is not empty");


        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(List.of(task1), history, "History is incorrect");


        historyManager.add(task2);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(List.of(task1, task2), history, "History is incorrect");


        historyManager.add(task1);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(List.of(task2, task1), history, "History is incorrect");
    }
}
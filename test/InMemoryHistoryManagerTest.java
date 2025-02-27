import tasks.Task;
import controllers.HistoryManager;
import controllers.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefaultHistory();
        LocalDateTime now = LocalDateTime.now();
        task1 = new Task("task1", "task description", TaskType.TASK, Duration.ofMinutes(30), now);
        task1.setId(0);
        task2 = new Task("task2", "task2 description", TaskType.TASK, Duration.ofMinutes(45), now.plusHours(1));
        task2.setId(1);
        task3 = new Task("task3", "task3 description", TaskType.TASK, Duration.ofMinutes(60), now.plusHours(2));
        task3.setId(2);
        task4 = new Task("task4", "task4 description", TaskType.TASK, Duration.ofMinutes(90), now.plusHours(3));
        task4.setId(3);
    }

    @Test
    public void add() {
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is null");
        assertEquals(1, history.size(), "History should contain 1 task");
    }

    @Test
    public void checkEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is null");
        assertEquals(0, history.size(), "History should be empty");
    }

    @Test
    void checkAddOneTaskTwice() {
        historyManager.add(task1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "History should contain 1 task after duplicate add");
    }

    @Test
    void checkDeleteLastTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task1, task2), history, "History should not contain removed task");
    }

    @Test
    void checkGetTaskListOrder() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task2, task1), history, "History order should reflect last added task");
    }
}
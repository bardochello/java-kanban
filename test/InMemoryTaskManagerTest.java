import controllers.Manager;
import controllers.TaskManager;
import tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;
    Epic epic;
    SubTask subTask1;
    SubTask subTask2;
    Task task;
    LocalDateTime now;

    @BeforeEach
    public void beforeEach() {
        taskManager = Manager.getDefault();
        now = LocalDateTime.now();
        task = new Task("task1", "task1 description", TaskType.TASK, Duration.ofMinutes(60), now);
        taskManager.addTask(task);

        epic = new Epic("epic1", "epic1 description");
        taskManager.addEpic(epic);

        subTask1 = new SubTask("subtask1", "subtask1 description", epic.getId(), Duration.ofMinutes(30), now.plusHours(1));
        subTask2 = new SubTask("subtask2", "subtask2 description", epic.getId(), Duration.ofMinutes(45), now.plusHours(2));
        taskManager.addTask(subTask1);
        taskManager.addTask(subTask2);
    }

    @Test
    public void testEpicStatusWithAllNewSubtasks() {
        assertEquals(Status.NEW, epic.getStatus(), "Epic status should be NEW with all NEW subtasks");
    }

    @Test
    public void testEpicStatusWithAllDoneSubtasks() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subTask1);
        taskManager.updateSubtask(subTask2);
        assertEquals(Status.DONE, epic.getStatus(), "Epic status should be DONE with all DONE subtasks");
    }

    @Test
    public void testEpicStatusWithMixedSubtasks() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.NEW);
        taskManager.updateSubtask(subTask1);
        taskManager.updateSubtask(subTask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Epic status should be IN_PROGRESS with mixed subtasks");
    }

    @Test
    public void testEpicFieldsCalculation() {
        assertEquals(now.plusHours(1), epic.getStartTime(), "Epic start time should match earliest subtask");
        assertEquals(Duration.ofMinutes(75), epic.getDuration(), "Epic duration should sum subtask durations");
        assertEquals(now.plusHours(2).plusMinutes(45), epic.getEndTime(), "Epic end time should match latest subtask");
    }

    @Test
    public void testPrioritizedTasks() {
        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(List.of(task, subTask1, subTask2), prioritized, "Tasks should be prioritized by start time");
    }

    @Test
    public void testTaskOverlap() {
        Task overlappingTask = new Task("overlap", "desc", TaskType.TASK, Duration.ofMinutes(30), now.plusMinutes(30));
        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(overlappingTask), "Should throw exception on time overlap");
    }
}
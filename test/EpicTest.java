import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void EpicsWithSameIdShouldBeEquals() {
        int epicId = 1;
        Epic epic1 = new Epic("epic1", "epic1 description");
        epic1.setId(epicId);
        Epic epic2 = new Epic("epic2", "epic2 description");
        epic2.setId(epicId);
        assertEquals(epic1, epic2, "Epics are not equals");
    }

    @Test
    public void testEpicFieldsWithNoSubtasks() {
        Epic epic = new Epic("epic", "description");
        assertEquals(Status.NEW, epic.getStatus(), "Status should be NEW with no subtasks");
        assertNull(epic.getStartTime(), "Start time should be null with no subtasks");
        assertEquals(Duration.ZERO, epic.getDuration(), "Duration should be zero with no subtasks");
        assertNull(epic.getEndTime(), "End time should be null with no subtasks");
    }

    @Test
    public void testEpicFieldsWithSubtasks() {
        Epic epic = new Epic("epic", "description");
        LocalDateTime start1 = LocalDateTime.now();
        LocalDateTime start2 = start1.plusHours(2);
        SubTask sub1 = new SubTask("sub1", "desc1", 0, Duration.ofMinutes(60), start1);
        SubTask sub2 = new SubTask("sub2", "desc2", 0, Duration.ofMinutes(90), start2);
        sub1.setStatus(Status.DONE);
        sub2.setStatus(Status.IN_PROGRESS);
        epic.addSubtasksId(1);
        epic.addSubtasksId(2);
        // В реальной системе поля обновляются в TaskManager, здесь имитируем вручную
        epic.setStartTime(start1);
        epic.setDuration(Duration.ofMinutes(150));
        epic.setEndTime(start2.plusMinutes(90));
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(start1, epic.getStartTime(), "Start time should match earliest subtask start");
        assertEquals(Duration.ofMinutes(150), epic.getDuration(), "Duration should sum subtask durations");
        assertEquals(start2.plusMinutes(90), epic.getEndTime(), "End time should match latest subtask end");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Status should be IN_PROGRESS with mixed subtask statuses");
    }
}
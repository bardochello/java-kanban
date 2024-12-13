import Tasks.SubTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    public void SubtasksWithSameIdShouldBeEquals() {
        int subTaskId = 1;
        SubTask subTask = new SubTask("subTask1", "subTask1 description", 0);
        subTask.setId(subTaskId);

        SubTask subTask2 = new SubTask("subTask2", "subTask2 description", 0);
        subTask2.setId(subTaskId);

        assertEquals(subTask, subTask2, "SubTasks are not equals");
    }
}
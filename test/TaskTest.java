import tasks.Task;
import org.junit.jupiter.api.Test;
import tasks.TaskType;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void TasksWithSameIdShouldBeEquals() {
        int taskId = 1;
        Task task1 = new Task("task1", "task1 description", TaskType.TASK);
        task1.setId(taskId);
        Task task2 = new Task("task2", "task2 description", TaskType.TASK);
        task2.setId(taskId);
        assertEquals(task1, task2, "Tasks are not equals");
    }
}
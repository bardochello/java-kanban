import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void TasksWithSameIdShouldBeEquals() {
        int taskId = 1;
        Task task1 = new Task("task1", "task1 description");
        task1.setId(taskId);
        Task task2 = new Task("task2", "task2 description");
        task2.setId(taskId);
        assertEquals(task1, task2, "Tasks are not equals");
    }
}
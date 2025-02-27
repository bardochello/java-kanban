import controllers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        File file = Files.createTempFile("test", ".csv").toFile();
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.deleteTasks();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(loadedManager.getTasks().isEmpty(), "Tasks list should be empty");
        assertTrue(loadedManager.getEpics().isEmpty(), "Epics list should be empty");
        assertTrue(loadedManager.getSubtasks().isEmpty(), "Subtasks list should be empty");
    }

    @Test
    void saveAndLoadTasks() throws IOException {
        File file = Files.createTempFile("test", ".csv").toFile();
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task("Task 1", "Description 1", TaskType.TASK, Duration.ofMinutes(60), startTime);
        manager.addTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        Task loadedTask = loadedManager.getTaskByID(task.getId());
        assertNotNull(loadedTask, "Загруженная задача не должна быть null");
        assertEquals(task.getDuration(), loadedTask.getDuration(), "Длительность задачи должна совпадать");
        assertEquals(task.getStartTime(), loadedTask.getStartTime(), "Время начала задачи должно совпадать");
    }
}
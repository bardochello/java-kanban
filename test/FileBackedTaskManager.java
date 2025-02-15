import controllers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        File file = Files.createTempFile("test", ".csv").toFile();
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Сохраняем пустой менеджер
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(loadedManager.getTasks().isEmpty());
        assertTrue(loadedManager.getEpics().isEmpty());
        assertTrue(loadedManager.getSubtasks().isEmpty());
    }

    @Test
    void saveAndLoadTasks() throws IOException {
        File file = Files.createTempFile("test", ".csv").toFile();
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Добавляем задачи
        Task task = new Task("Task 1", "Description 1", TaskType.TASK);
        Epic epic = new Epic("Epic 1", "Description 1", TaskType.EPIC);
        SubTask subtask = new SubTask("Subtask 1", "Description 1", epic.getId());
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addTask(subtask);

        // Сохраняем и загружаем
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, loadedManager.getTasks().size());
        assertEquals(1, loadedManager.getEpics().size());
        assertEquals(1, loadedManager.getSubtasks().size());
    }
}
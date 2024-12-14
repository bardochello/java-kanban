import Controllers.Manager;
import Controllers.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;
    Epic epic;
    SubTask subTask;
    Task task;
    int subTaskId;
    int epicId;
    int taskId;


    @BeforeEach
    public void beforeEach() {
        taskManager = Manager.getDefault();
        task = new Task("task1", "task1 description");

        epic = new Epic("epic1", "epic1 description");
        epicId = taskManager.addTask(epic);


        taskId = taskManager.addTask(task);

        taskManager.addEpic(epic);

        subTask = new SubTask("subtask1", "subtask1 description", 4);
        subTaskId = taskManager.addTask(subTask);
    }

    @Test
    public void epicCantBeSubtaskForItself() {
        SubTask testSubTask = taskManager.getSubtaskByID(subTaskId);
        assertNotNull(testSubTask, "Subtask didn't find");
        assertEquals(testSubTask.getEpicID(), epic.getId(), "data.Epic ID are not equals");
        assertNotEquals(testSubTask.getId(), epic.getId(), "ID equals");
    }

    @Test
    public void subtaskCantBeEpicForItself() {
        SubTask subtaskByID = taskManager.getSubtaskByID(subTaskId);
        assertNotNull(subtaskByID, "Подзадача не найдена.");
        subtaskByID.setEpicID(subTaskId);
        taskManager.updateSubtask(subtaskByID);
        assertEquals(subtaskByID.getEpicID(), subtaskByID.getId(), "ID равны");
    }

    @Test
    public void addEpic() {
        Epic testTask = taskManager.getEpicByID(epicId);
        List<Epic> epics = taskManager.getEpics();
        Epic epicTest = taskManager.getEpicByID(epicId);
        assertNotNull(testTask, "data.Epic didn't found");
        assertEquals(epic, testTask, "Epics are not equals");
        assertNotNull(epics, "There are not epic");
        assertNotNull(epicTest, "data.Epic can't be find by ID");
    }

    @Test
    public void epicNotChange() {
        Epic epicById = taskManager.getEpicByID(epicId);
        assertEquals(epic.getName(), epicById.getName(), "Names are not equals");
        assertEquals(epic.getDescription(), epicById.getDescription(), "Descriptions are not equals");
    }


    @Test
    public void addSubTask() {
        SubTask newSubTask = taskManager.getSubtaskByID(subTaskId);
        List<SubTask> subTasks = taskManager.getSubtasks();
        SubTask subTaskTest = taskManager.getSubtaskByID(subTaskId);
        assertNotNull(newSubTask, "Subtask didn't found");
        assertNotNull(subTaskTest, "data.Epic can't be find by ID");
        assertNotNull(subTasks, "Subtasks didn't find");
    }

    @Test
    public void subtaskNotChange() {
        SubTask testSubTask = taskManager.getSubtaskByID(subTaskId);
        assertEquals(subTask.getName(), testSubTask.getName(), "Names are not equals");
        assertEquals(subTask.getDescription(), testSubTask.getDescription(), "Descriptions are not equals");
        assertEquals(subTask.getEpicID(), testSubTask.getEpicID(), "Epics are not equals");
    }

    @Test
    public void addTask() {
        Task taskTest = taskManager.getTaskByID(taskId);
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(taskTest, "data.Task didn't find");
        assertNotNull(tasks, "Tasks didn't find");
    }

    @Test
    public void taskNotChange() {
        Task taskTest = taskManager.getTaskByID(taskId);
        assertEquals(task.getName(), taskTest.getName(), "Names are not equals");
        assertEquals(task.getDescription(), taskTest.getDescription(), "Descriptions are not equals");
    }
}
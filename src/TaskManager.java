import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();

    private int id = 1;

    public Task addTask(Task task) {
        task.setId(getID());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic addEpic(Epic epic) {
        epic.setId(getID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask addSubtask(SubTask subtask) {
        subtask.setId(getID());
        Epic epic = epics.get(subtask.getEpicID());
        epic.addSubtasksId(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    public Task updateTask(Task task) {
        Integer taskID = task.getId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    public void updateEpic(Epic epic) {
        Epic oldEpic = getEpicByID(epic.getId());
        epic.setSubtasksId(oldEpic.getSubTasksId());
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(SubTask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicByID(subtask.getEpicID());
        updateEpicStatus(epic);
    }

    public Task getTaskByID(int id) {
        return tasks.get(id);
    }

    public Epic getEpicByID(int id) {
        return epics.get(id);
    }

    public SubTask getSubtaskByID(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<SubTask> getEpicSubtasks(Epic epic) {
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        for (Integer id : epic.getSubTasksId()) {
            epicSubTasks.add(subtasks.get(id));
        }

        return epicSubTasks;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    public void deleteTaskByID(int id) {
        tasks.remove(id);
    }

    public void deleteEpicByID(int id) {
        Epic epic = getEpicByID(id);
        for (SubTask epicSubTask : getEpicSubtasks(epic)) {
            subtasks.remove(epicSubTask.getId());
        }
        epics.remove(id);
    }

    public void deleteSubtaskByID(int id) {
        SubTask subTask = getSubtaskByID(id);
        Epic subTaskEpic = getEpicByID(subTask.getEpicID());
        subTaskEpic.getSubTasksId().remove(id);
        subtasks.remove(id);
    }

    private int getID() {
        return id++;
    }

    private void updateEpicStatus(Epic epic) {
        int allIsDoneCount = 0;
        int allIsInNewCount = 0;
        ArrayList<SubTask> list = getEpicSubtasks(epic);

        for (SubTask subtask : list) {
            if (subtask.getStatus() == Status.DONE) {
                allIsDoneCount++;
            }
            if (subtask.getStatus() == Status.NEW) {
                allIsInNewCount++;
            }
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }

        if (allIsDoneCount == list.size()) {
            epic.setStatus(Status.DONE);
        } else if (allIsInNewCount == list.size()) {
            epic.setStatus(Status.NEW);
        }
    }
}

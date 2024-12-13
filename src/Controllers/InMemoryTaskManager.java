import data.Epic;
import data.Status;
import data.SubTask;
import data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager{

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    private int id = 1;

    @Override
    public int addTask(Task task) {
        int taskId = getID();
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId;
    }

    @Override
    public int addTask(SubTask subTask) {
        int taskId = getID();
        subTask.setId(taskId);
        subtasks.put(taskId, subTask);
        if (subTask.getEpicID() >= 0) {
            if (findEpic(subTask.getEpicID())) {
                epics.get(subTask.getEpicID()).addSubtasksId(taskId);;
                updateEpicStatus(epics.get(subTask.getEpicID()));
            }
        }
        return taskId;
    }

    @Override
    public int addTask(Epic epic) {
        int taskId = getID();
        epic.setId(taskId);
        epics.put(taskId, epic);
        return taskId;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addSubtask(SubTask subtask) {
        subtask.setId(getID());
        Epic epic = epics.get(subtask.getEpicID());
        epic.addSubtasksId(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskID = task.getId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = getEpicByID(epic.getId());
        epic.setSubtasksId(oldEpic.getSubTasksId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        SubTask newSubTask = subtasks.get(subtask.getId());

        newSubTask.setName(subtask.getName());
        newSubTask.setDescription(subtask.getDescription());
        newSubTask.setStatus(subtask.getStatus());

        if(findEpic(newSubTask.getEpicID())) {
            updateEpicStatus(epics.get(newSubTask.getEpicID()));
        }
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    public int getId() {
        return id;
    }

    @Override
    public SubTask getSubtaskByID(int id) {
        SubTask subTask = subtasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }



    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<SubTask> getEpicSubtasks(Epic epic) {
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        for (Integer id : epic.getSubTasksId()) {
            epicSubTasks.add(subtasks.get(id));
        }

        return epicSubTasks;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void deleteTaskByID(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) {
        Epic epic = getEpicByID(id);
        for (SubTask epicSubTask : getEpicSubtasks(epic)) {
            subtasks.remove(epicSubTask.getId());
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskByID(int id) {
        SubTask subTask = getSubtaskByID(id);
        Epic subTaskEpic = getEpicByID(subTask.getEpicID());
        subTaskEpic.getSubTasksId().remove(id);
        subtasks.remove(id);
    }



    private boolean findEpic(int id) {
        return epics.containsKey(id);
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

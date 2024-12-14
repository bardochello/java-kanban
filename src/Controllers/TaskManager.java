package Controllers;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int addTask(Task task);

    int addTask(SubTask subtask);

    int addTask(Epic epic);

    public Epic addEpic(Epic epic);

    public SubTask addSubtask(SubTask subtask);

    public Task updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubtask(SubTask subtask);

    public Task getTaskByID(int id);

    public Epic getEpicByID(int id);

    public SubTask getSubtaskByID(int id);

    public List<Task> getHistory();

    public List<Task> getTasks();

    public List<Epic> getEpics();

    public List<SubTask> getSubtasks();

    public List<SubTask> getEpicSubtasks(Epic epic);

    public void deleteTasks();

    public void deleteEpics();

    public void deleteSubtasks();

    public void deleteTaskByID(int id);

    public void deleteEpicByID(int id);

    public void deleteSubtaskByID(int id);
}

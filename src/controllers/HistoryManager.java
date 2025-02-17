package controllers;

import tasks.Task;

import java.util.List;

public interface HistoryManager { //интерфейс истории задач

    void add(Task task); //метод добавляющий просмотренную задачу в историю просмотров

    void remove(int id); //метод для удаления задачи из истории просмотров

    List<Task> getHistory(); //метод для возвращения просмотренных задач
}

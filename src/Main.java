import controllers.Manager;
import controllers.TaskManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();

        Task buyHouse = new Task("Купить дом", "Без мам, пап и кредитов", TaskType.TASK, Duration.ofMinutes(60), LocalDateTime.now());
        int buyHouseTask = taskManager.addTask(buyHouse);

        Task watchMovie = new Task("Посмотреть фильм", "Посмотреть фильм \"Бивень\"", TaskType.TASK, Duration.ofMinutes(90), LocalDateTime.now().plusHours(2));
        int watchMovieTask = taskManager.addTask(watchMovie);

        System.out.println("Prioritized Tasks: " + taskManager.getPrioritizedTasks());

        Epic getFreedom = new Epic("Обрести свободу", "Сделать как можно быстрее");
        taskManager.addEpic(getFreedom);
        SubTask freedomSub1 = new SubTask("Познакомиться с Тайлером", "Не в самолете", getFreedom.getId(), Duration.ofMinutes(30), LocalDateTime.now().plusDays(1));
        SubTask freedomSub2 = new SubTask("Потерять всё", "Лишь потеряв всё...", getFreedom.getId(), Duration.ofMinutes(45), LocalDateTime.now().plusDays(1).plusHours(1));
        taskManager.addTask(freedomSub1);
        taskManager.addTask(freedomSub2);
        freedomSub1.setStatus(Status.DONE);
        freedomSub2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(freedomSub1);
        taskManager.updateSubtask(freedomSub2);
        System.out.println("Epic Freedom: " + getFreedom);
        System.out.println("Subtasks: " + taskManager.getEpicSubtasks(getFreedom));

        System.out.println("Prioritized Tasks: " + taskManager.getPrioritizedTasks());
    }
}
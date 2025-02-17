import controllers.Manager;
import controllers.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();
        Task buyHouse = new Task("Купить дом", "Без мам, пап и кредитов", TaskType.TASK);
        int buyHouseTask = taskManager.addTask(buyHouse);

        Task watchMovie = new Task("Посмотреть фильм", "Посмотреть фильм \"Бивень\"", TaskType.TASK);
        int learnEnglishTask = taskManager.addTask(watchMovie);

        Task updateBuyHouse = new Task("Купить трехэтажный дом", "C помощью мамы, папы и с кредитом", buyHouse.getId(), Status.IN_PROGRESS, TaskType.TASK);
        Task updatedBuyHouse = taskManager.updateTask(updateBuyHouse);
        System.out.println(updatedBuyHouse);

        Task updateWatchMovie = new Task("Посмотреть фильм", "Посмотреть фильм \"Свадебная ваза\"", watchMovie.getId(), Status.DONE, TaskType.TASK);
        int updatedWatchMovie = taskManager.addTask(updateWatchMovie);


        Epic getFreedom = new Epic("Обрести свободу", "Сделать как можно быстрее", TaskType.EPIC);
        taskManager.addEpic(getFreedom);
        System.out.println(getFreedom);
        SubTask getFreedomSubTaskOne = new SubTask("Познакомиться с Тайлером Дерденом", "Желательно не в самолете", getFreedom.getId());
        SubTask getFreedomSubTaskTwo = new SubTask("Потерять всё", "Лишь потеряв всё, мы приобретаем свободу", getFreedom.getId());
        taskManager.addTask(getFreedomSubTaskOne);
        taskManager.addTask(getFreedomSubTaskTwo);
        getFreedomSubTaskOne.setStatus(Status.DONE);
        getFreedomSubTaskTwo.setStatus(Status.DONE);
        taskManager.updateSubtask(getFreedomSubTaskOne);
        taskManager.updateSubtask(getFreedomSubTaskTwo);
        System.out.println(taskManager.getEpicSubtasks(getFreedom));
        System.out.println(getFreedom);

        Epic getALife = new Epic("Начать новую жизнь", "С понедельника", TaskType.EPIC);
        taskManager.addEpic(getALife);
        System.out.println(getALife);
        SubTask getALifeSubTaskOne = new SubTask("Удалить доту", "Этим всё сказано", getALife.getId());
        taskManager.addTask(getALifeSubTaskOne);
        getALifeSubTaskOne.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(getALifeSubTaskOne);
        System.out.println(taskManager.getEpicSubtasks(getALife));
        System.out.println(getALife);

        System.out.println("Смотрим историю: ");
        taskManager.getTaskByID(1);
        System.out.println(taskManager.getHistory());

    }
}
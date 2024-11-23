public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task buyHouse = new Task("Купить дом", "Без мам, пап и кредитов");
        Task buyHouseTask = taskManager.addTask(buyHouse);
        System.out.println(buyHouseTask);

        Task watchMovie = new Task("Посмотреть фильм", "Посмотреть фильм \"Бивень\"");
        Task learnEnglishTask = taskManager.addTask(watchMovie);
        System.out.println(learnEnglishTask);

        Task updateBuyHouse = new Task("Купить трехэтажный дом", "C помощью мамы, папы и с кредитом", buyHouse.getId(), Status.IN_PROGRESS);
        Task updatedBuyHouse = taskManager.updateTask(updateBuyHouse);
        System.out.println(updatedBuyHouse);

        Task updateWatchMovie = new Task("Посмотреть фильм", "Посмотреть фильм \"Свадебная ваза\"", watchMovie.getId(), Status.DONE);
        Task updatedWatchMovie = taskManager.addTask(updateWatchMovie);
        System.out.println(updatedWatchMovie);


        Epic getFreedom = new Epic("Обрести свободу", "Сделать как можно быстрее");
        taskManager.addEpic(getFreedom);
        System.out.println(getFreedom);
        SubTask getFreedomSubTaskOne = new SubTask("Познакомиться с Тайлером Дерденом", "Желательно не в самолете", getFreedom.getId());
        SubTask getFreedomSubTaskTwo = new SubTask("Потерять всё", "Лишь потеряв всё, мы приобретаем свободу", getFreedom.getId());
        taskManager.addSubtask(getFreedomSubTaskOne);
        taskManager.addSubtask(getFreedomSubTaskTwo);
        getFreedomSubTaskOne.setStatus(Status.DONE);
        getFreedomSubTaskTwo.setStatus(Status.DONE);
        taskManager.updateSubtask(getFreedomSubTaskOne);
        taskManager.updateSubtask(getFreedomSubTaskTwo);
        System.out.println(taskManager.getEpicSubtasks(getFreedom));
        System.out.println(getFreedom);

        Epic getALife = new Epic("Начать новую жизнь", "С понедельника");
        taskManager.addEpic(getALife);
        System.out.println(getALife);
        SubTask getALifeSubTaskOne = new SubTask("Удалить доту", "Этим всё сказано", getALife.getId());
        taskManager.addSubtask(getALifeSubTaskOne);
        getALifeSubTaskOne.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(getALifeSubTaskOne);
        System.out.println(taskManager.getEpicSubtasks(getALife));
        System.out.println(getALife);
    }
}
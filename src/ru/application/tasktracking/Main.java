package ru.application.tasktracking;


public class Main {

    public static void main(String[] args) {

        /*Path myFile = Paths.get("Tasks.csv");

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(myFile);


        //SP6
        System.out.println();

        Task task1 = new Task("Task #taskId1", "Task description", StatusTask.NEW);
        int taskId1 = fileBackedTasksManager.creationTask(task1);
        Epic epic1 = new Epic("Epic #epicId1", "Epic description", StatusTask.NEW);
        int epicId1 = fileBackedTasksManager.creationEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #sub1", "Subtask description", StatusTask.NEW, epicId1);
        int sub1 = fileBackedTasksManager.creationSubtask(subtask1);

        System.out.println("таск - " + fileBackedTasksManager.getTasks());
        System.out.println("эпики - " + fileBackedTasksManager.getEpics());
        System.out.println("подзадачи - " + fileBackedTasksManager.getSubtasks());

        String string = fileBackedTasksManager.getSubtaskById(sub1).toString();
        string = fileBackedTasksManager.getEpicById(epicId1).toString();

        System.out.println("История " + fileBackedTasksManager.getHistory());

        */

    }
}
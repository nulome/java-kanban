package ru.application.tasktracking;


import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.service.HttpTaskManager;
import ru.application.tasktracking.servers.KVServer;
import ru.application.tasktracking.service.StatusTask;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /**
        Привет, Патимат!
*/

        new KVServer().start();

        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078/");

        Task task = new Task("Test1", "TestDescription", StatusTask.NEW);
        System.out.println(task.getUniqueId());









        /*KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/");
        kvTaskClient.put("file2", "{tsk:tsk}");
        System.out.println(kvTaskClient.load("file2"));

        kvTaskClient.put("file2", "{tsk:tskadsssssssss}");
        System.out.println(kvTaskClient.load("file2"));*/
        //httpTaskManager.creationTask(new Task("Test1", "TestDescription", StatusTask.NEW));
        //httpTaskManager.saveToServer();
        //httpTaskManager.save1();

        /*Integer checkTask = httpTaskManager.creationTask(new Task("TestNewTask", "TestDescription", StatusTask.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 0, 0)));
        Integer checkEpic = httpTaskManager.creationEpic(new Epic("TestNewEpic", "TestDescription", StatusTask.NEW));
        Subtask checkSubtask = new Subtask("TestNewSubtask", "TestDescription", StatusTask.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 2, 0), checkEpic);

        Task task = httpTaskManager.getTaskById(checkTask);

        TreeSet<Task> prior = null;
        HashMap<Integer, Epic> eplist = null;
        httpTaskManager.setEpicMap(eplist);
        httpTaskManager.setSortPrioritizedTasks(prior);
        httpTaskManager.loadFromServer();


        Epic ep = httpTaskManager.getEpicById(checkEpic);
        System.out.println(ep);
*/
        //httpTaskManager.loadFromServer();

        //System.out.println(task);


    }
}
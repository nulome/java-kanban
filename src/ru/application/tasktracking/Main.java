package ru.application.tasktracking;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.service.InMemoryTaskManager;
import ru.application.tasktracking.service.StatusTask;


public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();


        // Создание
        System.out.println("Создание");

        Epic epic1 = new Epic("Epic #epicId1", "Epic description", StatusTask.NEW);
        Epic epic2 = new Epic("Epic #epicId2", "Epic description", StatusTask.NEW);
        int epicId1 = inMemoryTaskManager.creationEpic(epic1);
        int epicId2 = inMemoryTaskManager.creationEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #sub1", "Subtask description", StatusTask.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask #sub2", "Subtask description", StatusTask.NEW, epicId1);
        Subtask subtask3 = new Subtask("Subtask #sub3", "Subtask description", StatusTask.NEW, epicId2);
        Subtask subtask4 = new Subtask("Subtask #sub4", "Subtask description", StatusTask.NEW, epicId1);
        int sub1 = inMemoryTaskManager.creationSubtask(subtask1);
        int sub2 = inMemoryTaskManager.creationSubtask(subtask2);
        int sub3 = inMemoryTaskManager.creationSubtask(subtask3);
        int sub4 = inMemoryTaskManager.creationSubtask(subtask4);
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());
        System.out.println("эпики - " + inMemoryTaskManager.getEpics());
        System.out.println("подз. эпик1 - " + inMemoryTaskManager.subtasksListToEpic(epicId1));
        System.out.println("подз. эпик2 - " + inMemoryTaskManager.subtasksListToEpic(epicId2));
        System.out.println();

        Task task1 = new Task("Task #taskId1", "Task description", StatusTask.NEW);
        int taskId1 = inMemoryTaskManager.creationTask(task1);
        System.out.println("таск - " + inMemoryTaskManager.getTasks());
        System.out.println();

        //История
        System.out.println();
        System.out.println("История");
        System.out.println("История " + inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getSubtaskById(sub1));
        System.out.println("История " + inMemoryTaskManager.getHistory());

        inMemoryTaskManager.delIdSubtaskMap(sub1);
        System.out.println("История " + inMemoryTaskManager.getHistory());

        subtask1.setStatus(StatusTask.DONE);
        subtask1.setUniqueId(sub1);
        inMemoryTaskManager.updateSubtask(subtask1);

        System.out.println(inMemoryTaskManager.getSubtaskById(sub1));
        System.out.println("История " + inMemoryTaskManager.getHistory());


        System.out.println(inMemoryTaskManager.getEpicById(epicId2));
        System.out.println(inMemoryTaskManager.getEpicById(epicId1));
        System.out.println("История " + inMemoryTaskManager.getHistory());

        System.out.println(inMemoryTaskManager.getEpicById(epicId2));
        System.out.println("История " + inMemoryTaskManager.getHistory());

        //удаление задачи из просмотра
        System.out.println();
        System.out.println("Удаление");
        System.out.println("История " + inMemoryTaskManager.getHistory());
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());
        inMemoryTaskManager.delIdSubtaskMap(sub1);
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());
        System.out.println("История " + inMemoryTaskManager.getHistory());
        System.out.print(inMemoryTaskManager.getTaskById(taskId1));
        System.out.print(inMemoryTaskManager.getEpicById(epicId1));
        System.out.print(inMemoryTaskManager.getEpicById(epicId2));
        System.out.println(inMemoryTaskManager.getSubtaskById(sub2));
        System.out.println("История " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.delIdTaskMap(taskId1);
        System.out.println("История " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.delIdEpicMap(epicId1);
        System.out.println("История " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.clearEpicMap();


        System.out.println("История " + inMemoryTaskManager.getHistory());
        System.out.println("эпики - " + inMemoryTaskManager.getEpics());
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());


    }
}
package ru.application.tasktracking;

import ru.application.tasktracking.service.InMemoryTaskManager;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;


public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        // Создание
        System.out.println("Создание");

        Epic epic1 = new Epic("Epic #1", "Epic description", "NEW");
        Epic epic2 = new Epic("Epic #2", "Epic description", "NEW");
        int epicId1 = inMemoryTaskManager.creationEpic(epic1);
        int epicId2 = inMemoryTaskManager.creationEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1", "Subtask description", "NEW", epicId1);
        Subtask subtask2 = new Subtask("Subtask #2", "Subtask description", "NEW", epicId1);
        Subtask subtask3 = new Subtask("Subtask #3", "Subtask description", "NEW", epicId2);
        int sub1 = inMemoryTaskManager.creationSubtask(subtask1);
        int sub2 = inMemoryTaskManager.creationSubtask(subtask2);
        int sub3 = inMemoryTaskManager.creationSubtask(subtask3);
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());
        System.out.println("эпики - " + inMemoryTaskManager.getEpics());
        System.out.println("подз. эпик1 - " + inMemoryTaskManager.subtasksListToEpic(epicId1));
        System.out.println("подз. эпик2 - " + inMemoryTaskManager.subtasksListToEpic(epicId2));
        System.out.println();

        Task task1 = new Task("Task #1", "Task description", "NEW");
        int taskId1 = inMemoryTaskManager.creationTask(task1);
        System.out.println("таск - " + inMemoryTaskManager.getTasks());
        System.out.println();

        // Обновление
        System.out.println("Обновление");

        subtask1.setStatus("DONE");
        subtask1.setUniqueId(sub1);
        subtask3.setStatus("DONE");
        subtask1.setUniqueId(sub3);
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.updateSubtask(subtask3);
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());
        System.out.println("эпики - " + inMemoryTaskManager.getEpics());
        System.out.println();

        // Удаление
        System.out.println("Удаление");

        inMemoryTaskManager.delIdEpicMap(epicId2);
        System.out.println("эпики - " + inMemoryTaskManager.getEpics());
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());
        inMemoryTaskManager.delIdSubtaskMap(sub1);
        inMemoryTaskManager.delIdSubtaskMap(sub1);
        System.out.println("эпики - " + inMemoryTaskManager.getEpics());
        System.out.println("подзадачи - " + inMemoryTaskManager.getSubtasks());


    }
}
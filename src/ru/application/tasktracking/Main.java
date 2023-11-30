package ru.application.tasktracking;

import ru.application.tasktracking.service.Manager;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

/*
Вроде теперь понял. Ранее думал, что у нас могут быть пустые задачи эпик с разными статусами.

*/

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        // Создание

        Epic epic1 = new Epic("Epic #1", "Epic description", "NEW");
        Epic epic2 = new Epic("Epic #2", "Epic description", "NEW");
        int epicId1 = manager.creationEpic(epic1);
        int epicId2 = manager.creationEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1", "Subtask description", "NEW", epicId1);
        Subtask subtask2 = new Subtask("Subtask #2", "Subtask description", "NEW", epicId1);
        Subtask subtask3 = new Subtask("Subtask #3", "Subtask description", "NEW", epicId2);
        int sub1 = manager.creationSubtask(subtask1);
        int sub2 = manager.creationSubtask(subtask2);
        int sub3 = manager.creationSubtask(subtask3);
        System.out.println("подзадачи - " + manager.getSubtasks());
        System.out.println("эпики - " + manager.getEpics());

        Task task1 = new Task("Task #1", "Task description", "NEW");
        int taskId1 = manager.creationTask(task1);
        System.out.println("таск - " + manager.getTasks());
        System.out.println();

        // Обновление

        subtask1.setStatus("DONE");
        subtask1.setUniqueId(sub1);
        subtask3.setStatus("DONE");
        subtask1.setUniqueId(sub3);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);
        System.out.println("подзадачи - " + manager.getSubtasks());
        System.out.println("эпики - " + manager.getEpics());
        System.out.println();

        // Удаление

        manager.delIdEpicMap(epicId2);
        System.out.println("эпики - " + manager.getEpics());
        System.out.println("подзадачи - " + manager.getSubtasks());
        manager.delIdSubtaskMap(sub1);
        System.out.println("эпики - " + manager.getEpics());
        System.out.println("подзадачи - " + manager.getSubtasks());


    }
}
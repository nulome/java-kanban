package ru.application.tasktracking;

import ru.application.tasktracking.service.Manager;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

/*
Как понял, все что тут находится для теста можно будет в дальнейшем сразу удалить, поэтому
    оставил комментарии тут для удобства, не стал удалять.
*/


public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        // Создание
        Task task1 = new Task("Task #1", "Task1 description", "NEW");
        int taskId1 = manager.creationTask(task1);
        System.out.println(manager.gettingListTask() + " / " + manager.getTaskMap());

        Epic epic1 = new Epic("Epic #1", "Epic description", "NEW");
        Epic epic2 = new Epic("Epic #2", "Epic description", "DONE");
        int epicId1 = manager.creationEpic(epic1);
        int epicId2 = manager.creationEpic(epic2);
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        System.out.println();

        // создание подзадачи и проверка статуса
        Subtask subtask1 = new Subtask("Subtask #1", "Subtask description", "NEW", 3);
        Subtask subtask2 = new Subtask("Subtask #2", "Subtask description", "DONE", 3);
        int sub1 = manager.creationSubtask(subtask1); // создаем задачу с NEW к Epic #2 (прежн статус DONE).
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        int sub2 = manager.creationSubtask(subtask2); // Epic #2 меняется на "IN_PROGRESS". подзадача с NEW и DONE.
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        System.out.println(manager.gettingListSubtask()  + " / " +  manager.getSubtaskMap());
        System.out.println();

        // Обновление
        Epic epic3 = new Epic("Epic #2", "Epic description", "DONE");
        epic3.setUniqueId(3);
        manager.updateEpic(epic3); // через обновление устанавливаем статус DONE для эпика с задачами - NEW и DONE.
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());

        Subtask subtask3 = new Subtask("Subtask #3", "Subtask description", "DONE", 3);
        subtask3.setUniqueId(4);
        manager.updateSubtask(subtask3); // обновляем подзадачу 4 на DONE.  Epic #2 меняется на "DONE".
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        System.out.println();


        // Удаление

        // -- удаление эпиков. Вместе с ними удаляются связанные подзадачи.
        /*
        manager.clearEpicMap();
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        System.out.println(manager.gettingListSubtask()  + " / " +  manager.getSubtaskMap());
        */

        manager.clearTaskMap();  // почистили обычные задачи
        System.out.println(manager.showListEpicToId(2)); //показать список подзадач эпика с ид 2 (пуст)
        manager.delIdEpicMap(2); //удалить эпик ид 2
        System.out.println(manager.showListEpicToId(3));
        manager.delIdSubtaskMap(5); // удалить подзадачу ид 5
        System.out.println(manager.showListEpicToId(3));
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        System.out.println(manager.gettingListSubtask()  + " / " +  manager.getSubtaskMap());
        System.out.println();


        // Проверка
        Subtask subtask4 = new Subtask("Subtask #3", "Subtask description", "NEW", 3);
        subtask4.setUniqueId(4);
        manager.updateSubtask(subtask4); // обновим статус на NEW

        System.out.println(manager.gettingListTask() + " / " + manager.getTaskMap());
        System.out.println(manager.gettingListEpic()  + " / " +  manager.getEpicMap());
        System.out.println(manager.gettingListSubtask()  + " / " +  manager.getSubtaskMap());

    }
}


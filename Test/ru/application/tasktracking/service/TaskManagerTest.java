package ru.application.tasktracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T manager;
    Task taskEmptyNEW;
    Integer numberTaskNEW;
    Epic epicEmptyNEW;
    Integer numberEpicNEW;
    Subtask subtaskNEWToEpicEmpty;
    Integer numberSubtaskNEW;


    private void testesFillingInTasks() {
        taskEmptyNEW = new Task("Test", "TestDescription", StatusTask.NEW);
        numberTaskNEW = manager.creationTask(taskEmptyNEW);

        epicEmptyNEW = new Epic("Test", "TestDescription", StatusTask.NEW);
        numberEpicNEW = manager.creationEpic(epicEmptyNEW);

        subtaskNEWToEpicEmpty = new Subtask("Test", "TestDescription", StatusTask.NEW, numberEpicNEW);
        numberSubtaskNEW = manager.creationSubtask(subtaskNEWToEpicEmpty);
    }

    @Test
    void checkingCreationOfTaskThroughManager() {
        Task taskTest = new Task("Test", "TestDescription", StatusTask.NEW, 1);
        Task taskNoId = new Task("Test", "TestDescription", StatusTask.NEW);
        Integer number = manager.creationTask(taskNoId);
        assertEquals(taskTest, manager.getTaskById(number), "Задачи не равны");
    }

    @Test
    void checkingCreationOfEpicThroughManager() {
        Epic taskTest = new Epic("Test", "TestDescription", StatusTask.NEW, 1);
        Epic taskNoId = new Epic("Test", "TestDescription", StatusTask.NEW);
        Integer number = manager.creationEpic(taskNoId);
        assertEquals(taskTest, manager.getEpicById(number), "Задачи не равны");
    }

    @Test
    void checkingCreationOfSubtaskThroughManager() {
        Epic epic = new Epic("Test", "TestDescription", StatusTask.NEW);
        Integer epicId1 = manager.creationEpic(epic);

        Subtask subtaskTest = new Subtask("Test", "TestDescription", StatusTask.NEW, epicId1);
        Integer number = manager.creationSubtask(subtaskTest);
        assertEquals(subtaskTest, manager.getSubtaskById(number), "Задачи не равны");

        Subtask subtaskNull = new Subtask("Test", "TestDescription", StatusTask.NEW, 7);
        RuntimeException exceptionCreateNoEpic = assertThrows(NullPointerException.class, () -> {
            manager.creationSubtask(subtaskNull);
        });
        assertNull(exceptionCreateNoEpic.getMessage());
    }

    @Test
    void checkingReceiptOfTaskByNumberTask() {
        Integer number = manager.creationTask(new Task("Test", "TestDescription", StatusTask.NEW));
        Task taskCheck = new Task("Test", "TestDescription", StatusTask.NEW, 1);
        assertEquals(taskCheck, manager.getTaskById(1), "Задачи не равны");

        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
            manager.getTaskById(2);
        });
        assertNull(exception.getMessage());
    }

    @Test
    void checkingReceiptOfTaskByNumberEpic() {
        Integer number = manager.creationEpic(new Epic("Test", "TestDescription", StatusTask.NEW));
        Epic taskCheck = new Epic("Test", "TestDescription", StatusTask.NEW, 1);
        assertEquals(taskCheck, manager.getEpicById(1), "Задачи не равны");

        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
            manager.getEpicById(2);
        });
        assertNull(exception.getMessage());
    }

    @Test
    void checkingReceiptOfTaskByNumberSubtask() {
        Integer elementaryEpic = manager.creationEpic(new Epic("Test", "TestDescription", StatusTask.NEW));
        Integer number = manager.creationSubtask(new Subtask("Test", "TestDescription", StatusTask.NEW, elementaryEpic));
        Subtask subtaskCheck = new Subtask("Test", "TestDescription", StatusTask.NEW, 2, elementaryEpic);

        assertEquals(subtaskCheck, manager.getSubtaskById(2), "Задачи не равны");

        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
            manager.getSubtaskById(1);
        });
        assertNull(exception.getMessage());
    }


    @Test
    void checkingDeletingFromCollectionByTaskID() {
        testesFillingInTasks();
        HashMap<Integer, Task> taskMapTestEmpty = new HashMap<>();
        manager.delIdTaskMap(numberTaskNEW);
        assertEquals(taskMapTestEmpty, manager.getTaskMap(), "Задача не удалена");
    }

    @Test
    void checkingDeletingFromCollectionByEpicID() {
        testesFillingInTasks();
        HashMap<Integer, Epic> taskMapTestEmpty = new HashMap<>();
        manager.delIdEpicMap(numberEpicNEW);
        assertEquals(taskMapTestEmpty, manager.getEpicMap(), "Задача не удалена");


        HashMap<Integer, Subtask> taskSubtaskMapTestEmpty = new HashMap<>();
        assertEquals(taskSubtaskMapTestEmpty, manager.getSubtaskMap(), "Задача не удалена");

    }

    @Test
    void checkingDeletingFromCollectionBySubtaskID() {
        testesFillingInTasks();
        HashMap<Integer, Subtask> taskSubtaskMapTestEmpty = new HashMap<>();

        manager.delIdSubtaskMap(numberSubtaskNEW);
        assertEquals(taskSubtaskMapTestEmpty, manager.getSubtaskMap(), "Задача не удалена");
    }

    @Test
    void checkingTasksCleaning() {
        testesFillingInTasks();
        HashMap<Integer, Task> taskMapTestEmpty = new HashMap<>();

        manager.clearTaskMap();
        assertEquals(taskMapTestEmpty, manager.getTaskMap(), "Задачи не почищены");
    }

    @Test
    void checkingEpicsCleaning() {
        testesFillingInTasks();
        HashMap<Integer, Epic> taskMapTestEmpty = new HashMap<>();
        HashMap<Integer, Subtask> taskSubtaskMapTestEmpty = new HashMap<>();

        manager.clearEpicMap();
        assertEquals(taskMapTestEmpty, manager.getEpicMap(), "Задачи не почищены");
        assertEquals(taskSubtaskMapTestEmpty, manager.getSubtaskMap(), "Задачи не почищены");
    }

    @Test
    void checkingSubtasksCleaning() {
        testesFillingInTasks();
        HashMap<Integer, Subtask> taskSubtaskMapTestEmpty = new HashMap<>();

        manager.clearSubtaskMap();
        assertEquals(taskSubtaskMapTestEmpty, manager.getSubtaskMap(), "Задачи не почищены");
    }

    @Test
    void checkingListTaskReturn() {
        testesFillingInTasks();
        taskEmptyNEW.setUniqueId(1);
        ArrayList<Task> taskListTest = new ArrayList<>();
        taskListTest.add(taskEmptyNEW);

        assertEquals(taskListTest, manager.getTasks(), "Списки задач не совпадают");
    }

    @Test
    void checkingListEpicReturn() {
        testesFillingInTasks();
        ArrayList<Epic> epicListTest = new ArrayList<>();
        epicListTest.add(epicEmptyNEW);

        assertEquals(epicListTest, manager.getEpics(), "Списки задач не совпадают");
    }

    @Test
    void checkingListSubtaskReturn() {
        testesFillingInTasks();
        ArrayList<Subtask> subtaskListTest = new ArrayList<>();
        subtaskListTest.add(subtaskNEWToEpicEmpty);

        assertEquals(subtaskListTest, manager.getSubtasks(), "Списки задач не совпадают");
    }


    @Test
    void checkingListSubtaskContainsToEpicReturn() {
        testesFillingInTasks();
        ArrayList<Subtask> subtaskListTest = new ArrayList<>();
        subtaskListTest.add(subtaskNEWToEpicEmpty);

        assertEquals(subtaskListTest, manager.subtasksListToEpic(numberEpicNEW), "Список подзадач по эпику не совпадает");
    }

    @Test
    void checkingUpdateTaskInManager() {
        testesFillingInTasks();
        Task checkTask = new Task("TestNewTask", "TestDescription", StatusTask.DONE, numberTaskNEW);
        manager.updateTask(new Task("TestNewTask", "TestDescription", StatusTask.DONE, numberTaskNEW));

        assertEquals(checkTask, manager.getTaskById(numberTaskNEW), "Задачи не равны");
    }

    @Test
    void checkingUpdateEpicInManager() {
        testesFillingInTasks();
        Epic checkEpic = new Epic("TestNewEpic", "TestDescription", StatusTask.NEW, numberEpicNEW);
        manager.updateEpic(new Epic("TestNewEpic", "TestDescription", StatusTask.DONE, numberEpicNEW));

        assertEquals(checkEpic, manager.getEpicById(numberEpicNEW), "Задачи не равны");
    }

    @Test
    void checkingUpdateSubtaskInManager() {
        testesFillingInTasks();
        Subtask checkSubtask = new Subtask("TestNewSubtask", "TestDescription", StatusTask.DONE, numberSubtaskNEW, numberEpicNEW);
        manager.updateSubtask(new Subtask("TestNewSubtask", "TestDescription", StatusTask.DONE, numberSubtaskNEW, numberEpicNEW));

        assertEquals(checkSubtask, manager.getSubtaskById(numberSubtaskNEW), "Задачи не равны");
    }

    @Test
    void checkingHistorySavingInOrderItWasAdded() {
        testesFillingInTasks();
        ArrayList<Task> tasksListTest = new ArrayList<>();
        assertEquals(tasksListTest, manager.getHistory(), "Истории не совпадают");

        tasksListTest.add(taskEmptyNEW);
        Task checkingngTask = manager.getTaskById(numberTaskNEW);
        assertEquals(tasksListTest, manager.getHistory(), "Истории не совпадают");

        tasksListTest.clear();
        tasksListTest.add(subtaskNEWToEpicEmpty);
        tasksListTest.add(epicEmptyNEW);
        tasksListTest.add(taskEmptyNEW);
        checkingngTask = manager.getEpicById(numberEpicNEW);
        checkingngTask = manager.getSubtaskById(numberSubtaskNEW);
        assertEquals(tasksListTest, manager.getHistory(), "Истории не совпадают");
    }

    @Test
    void checkingSortingOfTasksByTime() {
        testesFillingInTasks();
        Task checkTask = new Task("TestNewTask", "TestDescription", StatusTask.NEW, numberTaskNEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 0, 0));
        Epic checkEpic = new Epic("TestNewEpic", "TestDescription", StatusTask.NEW, numberEpicNEW);
        Subtask checkSubtask = new Subtask("TestNewSubtask", "TestDescription", StatusTask.NEW, numberSubtaskNEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 2, 0), numberEpicNEW);

        manager.updateTask(checkTask);
        manager.updateEpic(checkEpic);
        manager.updateSubtask(checkSubtask);

        List<Task> listTestCheck = new ArrayList<>();
        listTestCheck.add(checkTask);
        listTestCheck.add(checkSubtask);
        assertEquals(listTestCheck, manager.getPrioritizedTasks(), "Сортировка по приоритету не совпадает");

        Subtask checkSubtask2 = new Subtask("TestNewSubtask2", "TestDescription", StatusTask.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 0, 30), numberEpicNEW);
        int sub2 = manager.creationSubtask(checkSubtask2);
        Subtask checkSubtask3 = new Subtask("TestNewSubtask3", "TestDescription", StatusTask.NEW, numberEpicNEW);
        int sub3 = manager.creationSubtask(checkSubtask3);
        listTestCheck.add(1, checkSubtask2);
        listTestCheck.add(checkSubtask3);
        assertEquals(listTestCheck, manager.getPrioritizedTasks(), "Сортировка по приоритету не совпадает");

        manager.delIdSubtaskMap(sub2);
        listTestCheck.remove(1);
        assertEquals(listTestCheck, manager.getPrioritizedTasks(), "Сортировка по приоритету после удаления не совпадает");
    }
}
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
        final Task taskNoId = new Task("Test", "TestDescription", StatusTask.NEW);
        final Integer actual = manager.creationTask(taskNoId);
        final Integer expected = 1;
        assertEquals(expected, actual, "Неверное значение номера задачи");

        final Task taskExpected = manager.getTaskById(actual);
        assertNotNull(taskExpected, "Задача не найдена.");
        assertEquals(taskNoId, taskExpected, "Задачи не совпадают.");

        List<Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(taskNoId, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkingCreationOfEpicThroughManager() {
        Epic taskNoId = new Epic("Test", "TestDescription", StatusTask.NEW);
        Integer actual = manager.creationEpic(taskNoId);
        Integer expected = 1;
        assertEquals(expected, actual, "Неверное значение номера задачи");
    }

    @Test
    void checkingCreationOfSubtaskThroughManager() {
        Epic epic = new Epic("Test", "TestDescription", StatusTask.NEW);
        Integer epicId1 = manager.creationEpic(epic);

        Subtask subtaskTest = new Subtask("Test", "TestDescription", StatusTask.NEW, epicId1);
        Integer actual = manager.creationSubtask(subtaskTest);
        Integer expected = 2;
        assertEquals(expected, actual, "Неверное значение номера задачи");

        Subtask subtaskNull = new Subtask("Test", "TestDescription", StatusTask.NEW, 7);
        RuntimeException exceptionCreateNoEpic = assertThrows(NullPointerException.class, () -> {
            manager.creationSubtask(subtaskNull);
        });
        assertNull(exceptionCreateNoEpic.getMessage());
    }

    @Test
    void checkingReceiptOfTaskByNumberTask() {
        Integer number = manager.creationTask(new Task("Test", "TestDescription", StatusTask.NEW));
        Task taskExpected = new Task("Test", "TestDescription", StatusTask.NEW, 1);
        assertEquals(taskExpected, manager.getTaskById(1), "Задачи не равны");

        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
            manager.getTaskById(2);
        });
        assertNull(exception.getMessage());
    }

    @Test
    void checkingReceiptOfTaskByNumberEpic() {
        Integer number = manager.creationEpic(new Epic("Test", "TestDescription", StatusTask.NEW));
        Epic taskExpected = new Epic("Test", "TestDescription", StatusTask.NEW, 1);
        assertEquals(taskExpected, manager.getEpicById(1), "Задачи не равны");

        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
            manager.getEpicById(2);
        });
        assertNull(exception.getMessage());
    }

    @Test
    void checkingReceiptOfTaskByNumberSubtask() {
        Integer elementaryEpic = manager.creationEpic(new Epic("Test", "TestDescription", StatusTask.NEW));
        Integer number = manager.creationSubtask(new Subtask("Test", "TestDescription", StatusTask.NEW, elementaryEpic));
        Subtask subtaskExpected = new Subtask("Test", "TestDescription", StatusTask.NEW, 2, elementaryEpic);

        assertEquals(subtaskExpected, manager.getSubtaskById(2), "Задачи не равны");

        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
            manager.getSubtaskById(1);
        });
        assertNull(exception.getMessage());
    }


    @Test
    void checkingDeletingFromCollectionByTaskID() {
        testesFillingInTasks();
        HashMap<Integer, Task> expected = new HashMap<>();
        manager.delIdTaskMap(numberTaskNEW);
        assertEquals(expected, manager.getTaskMap(), "Задача не удалена");
    }

    @Test
    void checkingDeletingFromCollectionByEpicID() {
        testesFillingInTasks();
        HashMap<Integer, Epic> expectedEpic = new HashMap<>();
        manager.delIdEpicMap(numberEpicNEW);
        assertEquals(expectedEpic, manager.getEpicMap(), "Задача не удалена");


        HashMap<Integer, Subtask> expectedListSub = new HashMap<>();
        assertEquals(expectedListSub, manager.getSubtaskMap(), "Задача не удалена");

    }

    @Test
    void checkingDeletingFromCollectionBySubtaskID() {
        testesFillingInTasks();
        HashMap<Integer, Subtask> expected = new HashMap<>();

        manager.delIdSubtaskMap(numberSubtaskNEW);
        assertEquals(expected, manager.getSubtaskMap(), "Задача не удалена");
    }

    @Test
    void checkingTasksCleaning() {
        testesFillingInTasks();
        HashMap<Integer, Task> expected = new HashMap<>();

        manager.clearTaskMap();
        assertEquals(expected, manager.getTaskMap(), "Задачи не почищены");
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
        ArrayList<Task> taskListExpected = new ArrayList<>();
        taskListExpected.add(taskEmptyNEW);

        assertEquals(taskListExpected, manager.getTasks(), "Списки задач не совпадают");
    }

    @Test
    void checkingListEpicReturn() {
        testesFillingInTasks();
        ArrayList<Epic> epicListExpected = new ArrayList<>();
        epicListExpected.add(epicEmptyNEW);

        assertEquals(epicListExpected, manager.getEpics(), "Списки задач не совпадают");
    }

    @Test
    void checkingListSubtaskReturn() {
        testesFillingInTasks();
        ArrayList<Subtask> subtaskListExpected = new ArrayList<>();
        subtaskListExpected.add(subtaskNEWToEpicEmpty);

        assertEquals(subtaskListExpected, manager.getSubtasks(), "Списки задач не совпадают");
    }


    @Test
    void checkingListSubtaskContainsToEpicReturn() {
        testesFillingInTasks();
        ArrayList<Subtask> subtaskListExpected = new ArrayList<>();
        subtaskListExpected.add(subtaskNEWToEpicEmpty);

        assertEquals(subtaskListExpected, manager.subtasksListToEpic(numberEpicNEW), "Список подзадач по эпику не совпадает");
    }

    @Test
    void checkingUpdateTaskInManager() {
        testesFillingInTasks();
        Task expectedTask = new Task("TestNewTask", "TestDescription", StatusTask.DONE, numberTaskNEW);
        manager.updateTask(new Task("TestNewTask", "TestDescription", StatusTask.DONE, numberTaskNEW));

        assertEquals(expectedTask, manager.getTaskById(numberTaskNEW), "Задачи не равны");
    }

    @Test
    void checkingUpdateEpicInManager() {
        testesFillingInTasks();
        Epic expectedEpic = new Epic("TestNewEpic", "TestDescription", StatusTask.NEW, numberEpicNEW);
        manager.updateEpic(new Epic("TestNewEpic", "TestDescription", StatusTask.DONE, numberEpicNEW));

        assertEquals(expectedEpic, manager.getEpicById(numberEpicNEW), "Задачи не равны");
    }

    @Test
    void checkingUpdateSubtaskInManager() {
        testesFillingInTasks();
        Subtask expectedSubtask = new Subtask("TestNewSubtask", "TestDescription", StatusTask.DONE, numberSubtaskNEW, numberEpicNEW);
        manager.updateSubtask(new Subtask("TestNewSubtask", "TestDescription", StatusTask.DONE, numberSubtaskNEW, numberEpicNEW));

        assertEquals(expectedSubtask, manager.getSubtaskById(numberSubtaskNEW), "Задачи не равны");
    }

    @Test
    void checkingHistorySavingInOrderItWasAdded() {
        testesFillingInTasks();
        ArrayList<Task> tasksListExpected = new ArrayList<>();
        assertEquals(tasksListExpected, manager.getHistory(), "Истории не совпадают");

        tasksListExpected.add(taskEmptyNEW);
        Task checkingngTask = manager.getTaskById(numberTaskNEW);
        assertEquals(tasksListExpected, manager.getHistory(), "Истории не совпадают");

        tasksListExpected.clear();
        tasksListExpected.add(subtaskNEWToEpicEmpty);
        tasksListExpected.add(epicEmptyNEW);
        tasksListExpected.add(taskEmptyNEW);
        checkingngTask = manager.getEpicById(numberEpicNEW);
        checkingngTask = manager.getSubtaskById(numberSubtaskNEW);
        assertEquals(tasksListExpected, manager.getHistory(), "Истории не совпадают");
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

        List<Task> listExpected = new ArrayList<>();
        listExpected.add(checkTask);
        listExpected.add(checkSubtask);
        assertEquals(listExpected, manager.getPrioritizedTasks(), "Сортировка по приоритету не совпадает");

        Subtask checkSubtask2 = new Subtask("TestNewSubtask2", "TestDescription", StatusTask.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 0, 30), numberEpicNEW);
        int sub2 = manager.creationSubtask(checkSubtask2);
        Subtask checkSubtask3 = new Subtask("TestNewSubtask3", "TestDescription", StatusTask.NEW, numberEpicNEW);
        int sub3 = manager.creationSubtask(checkSubtask3);
        listExpected.add(1, checkSubtask2);
        listExpected.add(checkSubtask3);
        assertEquals(listExpected, manager.getPrioritizedTasks(), "Сортировка по приоритету не совпадает");

        manager.delIdSubtaskMap(sub2);
        listExpected.remove(1);
        assertEquals(listExpected, manager.getPrioritizedTasks(), "Сортировка по приоритету после удаления не совпадает");

        System.out.println(manager.getPrioritizedTasks());

        Task taskError = new Task("TestNewTask", "TestDescription", StatusTask.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 0, 0));
        ManagerSaveException exceptionAddTaskSameTime = assertThrows(ManagerSaveException.class, () -> {
            manager.creationTask(taskError);
        }, "Ожидалось исключение ManagerSaveException");
        assertEquals("Время для выполнения задачи уже занято.", exceptionAddTaskSameTime.getMessage());
    }
}
package ru.application.tasktracking.objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.application.tasktracking.service.InMemoryTaskManager;
import ru.application.tasktracking.service.StatusTask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private static InMemoryTaskManager memoryTaskManager;
    private static InMemoryTaskManager memoryTaskManagerSubtask;
    private static Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    private static int epicId1;
    private static int sub1;
    private static int sub2;


/*
    @BeforeEach
    void creatingTaskManager() {
        memoryTaskManager = new InMemoryTaskManager();
        memoryTaskManagerSubtask = new InMemoryTaskManager();
        epic1 = new Epic("Test Epic #epicId1", "Test Epic description", StatusTask.NEW);
        epicId1 = memoryTaskManager.creationEpic(epic1);
        epicId1 = memoryTaskManagerSubtask.creationEpic(epic1);

        subtask1 = new Subtask("Subtask #sub1", "Subtask description", StatusTask.NEW, epicId1);
        subtask2 = new Subtask("Subtask #sub2", "Subtask description", StatusTask.NEW, epicId1);
        sub1 = memoryTaskManagerSubtask.creationSubtask(subtask1);
        sub2 = memoryTaskManagerSubtask.creationSubtask(subtask2);
    }

    @Test
    public void checkingStatusWithAnEmptyList() {
        assertEquals(StatusTask.NEW, memoryTaskManager.getEpicById(epicId1).status, "Статусы не совпадают.");
    }

    @Test
    public void checkingStatusWithAnEmptyListOfStatusDone() {
        memoryTaskManager.updateEpic(new Epic("Test Epic #epicId1", "Test Epic description",
                StatusTask.DONE, epicId1));
        assertEquals(StatusTask.DONE, memoryTaskManager.getEpicById(epicId1).status, "Статусы не совпадают.");
    }



    @Test
    public void checkingStatusSfEpicFromChangingSubtasksStatusNew() {
        assertEquals(StatusTask.NEW, memoryTaskManagerSubtask.getEpicById(epicId1).status, "Статусы Эпик не совпадают.");
    }

    @Test
    public void checkingStatusSfEpicFromChangingSubtasksStatusDone() {
        memoryTaskManagerSubtask.updateSubtask(new Subtask("Subtask #sub1", "Subtask description",
                StatusTask.DONE, sub1, epicId1));
        memoryTaskManagerSubtask.updateSubtask(new Subtask("Subtask #sub2", "Subtask description",
                StatusTask.DONE, sub2, epicId1));
        assertEquals(StatusTask.DONE, memoryTaskManagerSubtask.getEpicById(epicId1).status, "Статусы Эпик не совпадают.");
    }

    @Test
    public void checkingStatusWithOneNewAndDone() {
        memoryTaskManagerSubtask.updateSubtask(new Subtask("Subtask #sub1", "Subtask description",
                StatusTask.DONE, sub1, epicId1));
        assertEquals(StatusTask.IN_PROGRESS, memoryTaskManagerSubtask.getEpicById(epicId1).status, "Статусы Эпик не совпадают.");
    }

    @Test
    public void checkingWithSubtasksInStatusProgress() {
        memoryTaskManagerSubtask.updateSubtask(new Subtask("Subtask #sub1", "Subtask description",
                StatusTask.IN_PROGRESS, sub1, epicId1));
        memoryTaskManagerSubtask.updateSubtask(new Subtask("Subtask #sub2", "Subtask description",
                StatusTask.IN_PROGRESS, sub2, epicId1));
        assertEquals(StatusTask.IN_PROGRESS, memoryTaskManagerSubtask.getEpicById(epicId1).status, "Статусы Эпик не совпадают.");
    }*/
}
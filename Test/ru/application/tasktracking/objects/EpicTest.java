package ru.application.tasktracking.objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.application.tasktracking.service.InMemoryTaskManager;
import ru.application.tasktracking.service.StatusTask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    InMemoryTaskManager memoryTaskManager;
    int createEpic;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void cerateMemory(){
        memoryTaskManager = new InMemoryTaskManager();
        epic1 = new Epic("#epicId1", "description", StatusTask.NEW);
        createEpic = memoryTaskManager.creationEpic(epic1); //1
    }

    private void createSubtask(StatusTask status1, StatusTask status2){

        subtask1 = new Subtask("#sub1", "description",
                status1, createEpic);
        subtask2 = new Subtask("#sub2", "description",
                status2, createEpic);
        int sub1 = memoryTaskManager.creationSubtask(subtask1);
        int sub2 = memoryTaskManager.creationSubtask(subtask2);
    }

    @Test
    public void checkingStatusWithAnEmptyList() {
        assertEquals(StatusTask.NEW, memoryTaskManager.getEpicById(createEpic).getStatus(), "Статусы не совпадают.");
    }


    @Test
    public void checkingStatusSfEpicFromChangingSubtasksStatusNew() {
        createSubtask(StatusTask.NEW, StatusTask.NEW);
        assertEquals(StatusTask.NEW, memoryTaskManager.getEpicById(createEpic).getStatus(), "Статусы не совпадают.");
    }


    @Test
    public void checkingStatusSfEpicFromChangingSubtasksStatusDone() {
        createSubtask(StatusTask.DONE, StatusTask.DONE);
        assertEquals(StatusTask.DONE, memoryTaskManager.getEpicById(createEpic).status, "Статусы Эпик не совпадают.");
    }

    @Test
    public void checkingStatusWithOneNewAndDone() {
        createSubtask(StatusTask.NEW, StatusTask.DONE);
        assertEquals(StatusTask.IN_PROGRESS, memoryTaskManager.getEpicById(createEpic).status, "Статусы Эпик не совпадают.");
    }

    @Test
    public void checkingWithSubtasksInStatusProgress() {
        createSubtask(StatusTask.IN_PROGRESS, StatusTask.IN_PROGRESS);
        assertEquals(StatusTask.IN_PROGRESS, memoryTaskManager.getEpicById(createEpic).status, "Статусы Эпик не совпадают.");
    }
}
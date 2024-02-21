package ru.application.tasktracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTest extends TaskManagerTest<FileBackedTasksManager> {

    private final String testNullFile = "Test/resources/FileNull.csv";

    @BeforeEach
    public void createManager() {
        manager = new FileBackedTasksManager(testNullFile);
    }

    @Test
    void checkingWorkPreservationAndRestorationOfState() {

        final String testSaveLoadManager = "Test/resources/TestSaveLoadManager.csv";
        FileBackedTasksManager fileManager = new FileBackedTasksManager(testSaveLoadManager);
        Task task1 = new Task("Test", "TestDescription", StatusTask.NEW);
        Integer num1 = fileManager.creationTask(task1);
        Epic task2 = new Epic("Test", "TestDescription", StatusTask.NEW);
        Integer num2 =  fileManager.creationEpic(task2);
        Subtask task3 = new Subtask("Test", "TestDescription", StatusTask.NEW, num2);
        Integer num3 =  fileManager.creationSubtask(task3);
        Integer num4 =  fileManager.creationSubtask(new Subtask("Test", "TestDescription", StatusTask.NEW, num2));
        Integer num5 =  fileManager.creationSubtask(new Subtask("Test", "TestDescription", StatusTask.NEW, num2));
        Integer num6 =  fileManager.creationEpic(new Epic("Test", "TestDescription", StatusTask.NEW));
        Integer num7 = fileManager.creationTask(new Task("Test", "TestDescription", StatusTask.NEW));
        Task getTask = fileManager.getTaskById(num7);
        getTask = fileManager.getSubtaskById(num3);
        getTask = fileManager.getSubtaskById(num5);


        FileBackedTasksManager fileManagerLoad = FileBackedTasksManager.loadFromFile(testSaveLoadManager);
        ArrayList<Task> getTasksLoad = fileManagerLoad.getTasks();
        ArrayList<Epic> getEpicsLoad = fileManagerLoad.getEpics();
        ArrayList<Subtask> getSubtasksLoad = fileManagerLoad.getSubtasks();

        assertNotNull(getTasksLoad, "Задачи на возвращаются.");
        assertNotNull(getEpicsLoad, "Задачи на возвращаются.");
        assertNotNull(getSubtasksLoad, "Задачи на возвращаются.");

        assertEquals(fileManager.getTasks().size(), getTasksLoad.size(), "Неверное количество задач.");
        assertEquals(fileManager.getEpics().size(), getEpicsLoad.size(), "Неверное количество задач.");
        assertEquals(fileManager.getSubtasks().size(), getSubtasksLoad.size(), "Неверное количество задач.");



        List<Task> getHistoryLoad = fileManagerLoad.getHistory();
        assertNotNull(getHistoryLoad, "История на возвращается.");
        assertEquals(fileManager.getHistory().size(), getHistoryLoad.size(), "Неверное количество задач.");

        Epic epicOld = fileManager.getEpicById(num6);
        Epic epicLoad = fileManagerLoad.getEpicById(num6);
        assertEquals(epicOld, epicLoad, "Задачи не равны.");
        ArrayList<Integer> listEpicLoad = epicLoad.getListSubtaskId();

        assertEquals(0, listEpicLoad.size(), "У эпика появились задачи");
    }

}

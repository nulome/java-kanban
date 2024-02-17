package ru.application.tasktracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    InMemoryHistoryManager historyManager;
    Task taskCheck1;
    Task taskCheck2;
    Task taskCheck3;
    Task taskCheck4;

    @BeforeEach
    void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();

        taskCheck1 = new Task("Test1", "TestDescription", StatusTask.NEW, 1);
        taskCheck2 = new Task("Test2", "TestDescription", StatusTask.NEW, 2);
        taskCheck3 = new Task("Test3", "TestDescription", StatusTask.NEW, 3);
        taskCheck4 = new Task("Test4", "TestDescription", StatusTask.NEW, 4);

    }

    @Test
    void addHistory() {
        historyManager.addHistory(taskCheck1);
        Node<Task> node1Expected = new Node<>(taskCheck1);
        Node<Task> node2Expected = new Node<>(taskCheck2);
        Node<Task> node3Expected = new Node<>(taskCheck3);

        Node actualNode = historyManager.getNodeHistory().get(1);
        assertEquals(node1Expected, actualNode, "Ноды истории не равны");

        historyManager.addHistory(taskCheck2);
        historyManager.addHistory(taskCheck3);
        actualNode = historyManager.getNodeHistory().get(2);
        assertEquals(node2Expected, actualNode, "Ноды истории не равны");
    }

    @Test
    void getHistory() {
        ArrayList<Task> historyExpected = new ArrayList<>();
        assertEquals(historyExpected, historyManager.getHistory(), "История не верная");

        historyManager.addHistory(taskCheck1);
        historyManager.addHistory(taskCheck4);
        historyManager.addHistory(taskCheck2);
        historyManager.addHistory(taskCheck3);
        historyExpected.add(taskCheck1);
        historyExpected.add(0,taskCheck4);
        historyExpected.add(0,taskCheck2);
        historyExpected.add(0,taskCheck3);
        assertEquals(historyExpected, historyManager.getHistory(), "История не верная");

        historyManager.addHistory(taskCheck4);
        historyExpected.remove(2);
        historyExpected.add(0,taskCheck4);
        assertEquals(historyExpected, historyManager.getHistory(), "История не верная");
    }

    @Test
    void removeHistory() {
        ArrayList<Task> historyExpected = new ArrayList<>();
        historyManager.addHistory(taskCheck1);
        historyManager.addHistory(taskCheck2);
        historyManager.addHistory(taskCheck3);
        historyManager.addHistory(taskCheck4);
        historyManager.removeHistory(3);
        historyExpected.add(taskCheck1);
        historyExpected.add(0,taskCheck2);
        historyExpected.add(0,taskCheck4);
        assertEquals(historyExpected, historyManager.getHistory(), "История не верная");

        historyManager.removeHistory(4);
        historyExpected.remove(0);
        assertEquals(historyExpected, historyManager.getHistory(), "История не верная");

        historyManager.removeHistory(1);
        historyExpected.remove(1);
        assertEquals(historyExpected, historyManager.getHistory(), "История не верная");
    }

}
package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void clearTaskMap();

    void clearEpicMap();

    void clearSubtaskMap();

    Task getTaskById(Integer key);

    Epic getEpicById(Integer key);

    Subtask getSubtaskById(Integer key);

    Integer creationTask(Task task);

    Integer creationEpic(Epic epic);

    Integer creationSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void delIdTaskMap(int id);

    void delIdEpicMap(int id);

    void delIdSubtaskMap(int id);

    ArrayList<Subtask> subtasksListToEpic(int id);

    List<Task> getHistory();
    List<Task> getPrioritizedTasks();

    HashMap<Integer, Task> getTaskMap();

    HashMap<Integer, Epic> getEpicMap();

    HashMap<Integer, Subtask> getSubtaskMap();
}

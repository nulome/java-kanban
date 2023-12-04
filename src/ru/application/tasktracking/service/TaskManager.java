package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;

public interface TaskManager {
    public ArrayList<Task> getTasks();

    public ArrayList<Epic> getEpics();

    public ArrayList<Subtask> getSubtasks();
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
    ArrayList<Task> getHistory();
}

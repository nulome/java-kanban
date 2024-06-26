package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> getHistory();
    void addHistory(Task task);
    void removeHistory(int id);
}

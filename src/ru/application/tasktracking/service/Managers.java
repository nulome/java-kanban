package ru.application.tasktracking.service;

public abstract class Managers {

    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public HistoryManager  getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}

package ru.application.tasktracking.service;

public final class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager(String file) {
        return FileBackedTasksManager.loadFromFile(file);
    }

    public static TaskManager getHttpTasksManager(String url) {
        return new HttpTaskManager(url);
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

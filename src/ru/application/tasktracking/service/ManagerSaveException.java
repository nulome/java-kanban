package ru.application.tasktracking.service;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException() {
    }

    public ManagerSaveException(String message) {
        super(message);
    }

    public ManagerSaveException(final String message, final IOException e) {
        super(message, e);
    }
}

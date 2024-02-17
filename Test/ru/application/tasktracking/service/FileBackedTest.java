package ru.application.tasktracking.service;

import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTest extends TaskManagerTest<FileBackedTasksManager>{

    private final Path testNullFile = Paths.get("FileNull.csv");

    @BeforeEach
    public void createManager() {
        manager = new FileBackedTasksManager(testNullFile);
    }


}

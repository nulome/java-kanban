package ru.application.tasktracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    public void createManager() {
        manager = new InMemoryTaskManager();
    }

}

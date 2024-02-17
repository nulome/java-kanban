package ru.application.tasktracking.objects;

import ru.application.tasktracking.service.StatusTask;
import ru.application.tasktracking.service.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String name, String description, StatusTask status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int uniqueId, int epicId) {
        super(name, description, status, uniqueId);
        this.epicId = epicId;
    }
    public Subtask(String name, String description, StatusTask status, int uniqueId, Duration duration,
                   LocalDateTime startTime, int epicId) {
        super(name, description, status, uniqueId, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask task = (Subtask) o;
        return name.equals(task.name) &&
                uniqueId == task.uniqueId &&
                status == task.status &&
                epicId == task.epicId;
    }
}

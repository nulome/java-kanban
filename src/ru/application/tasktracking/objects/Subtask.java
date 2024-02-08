package ru.application.tasktracking.objects;

import ru.application.tasktracking.service.StatusTask;
import ru.application.tasktracking.service.TypeTask;

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
}

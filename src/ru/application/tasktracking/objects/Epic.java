package ru.application.tasktracking.objects;

import ru.application.tasktracking.service.StatusTask;
import ru.application.tasktracking.service.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> listSubtaskId = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status);
    }


    public Epic(String name, String description, StatusTask status, int uniqueId) {
        super(name, description, status, uniqueId);
    }

    public Epic(String name, String description, StatusTask status, int uniqueId, Duration duration,
                LocalDateTime startTime) {
        super(name, description, status, uniqueId, duration, startTime);
    }


    public ArrayList<Integer> getListSubtaskId() {
        return listSubtaskId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setListSubtaskId(ArrayList<Integer> listSubtaskId) {
        this.listSubtaskId = listSubtaskId;
    }

    @Override
    public LocalDateTime getEndTime(){
        if(listSubtaskId.isEmpty()) {
            return startTime.plusMinutes(duration.toMinutes());
        }
        return endTime;
    }
    @Override
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                //", listSubtaskId=" + listSubtaskId +
                '}';
    }
}
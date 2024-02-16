package ru.application.tasktracking.objects;

import ru.application.tasktracking.service.StatusTask;
import ru.application.tasktracking.service.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected String name;
    protected String description;
    protected StatusTask status;
    protected int uniqueId;
    protected Duration duration = Duration.ofMinutes(0);
    protected LocalDateTime startTime;


    public Task(String name, String description, StatusTask status, int uniqueId, Duration duration,
                LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.uniqueId = uniqueId;
        this.duration = duration;
        this.startTime = startTime;
    }
    public Task(String name, String description, StatusTask status, int uniqueId) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.uniqueId = uniqueId;
    }

    public Task(String name, String description, StatusTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }



    public int getUniqueId() {
        return uniqueId;
    }
    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public StatusTask getStatus() {
        return status;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}

package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private int newId = 0;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void clearTaskMap() {
        taskMap.clear();
    }

    @Override
    public void clearEpicMap() {
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void clearSubtaskMap() {
        for (Epic epic : epicMap.values()) {
            epic.setListSubtaskId(new ArrayList<>());
            updateStatusEpic(epic);
        }
        subtaskMap.clear();
    }

    @Override
    public Task getTaskById(Integer key) {
        return taskMap.get(key);
    }

    @Override
    public Epic getEpicById(Integer key) {
        return epicMap.get(key);
    }

    @Override
    public Subtask getSubtaskById(Integer key) {
        return subtaskMap.get(key);
    }

    @Override
    public Integer creationTask(Task task) {
        this.newId++;
        task.setUniqueId(newId);
        taskMap.put(task.getUniqueId(), task);
        return newId;
    }

    @Override
    public Integer creationEpic(Epic epic) {
        this.newId++;
        epic.setUniqueId(newId);
        epicMap.put(epic.getUniqueId(), epic);
        return newId;
    }

    @Override
    public Integer creationSubtask(Subtask subtask) {
        this.newId++;
        subtask.setUniqueId(newId);
        subtaskMap.put(subtask.getUniqueId(), subtask);

        Epic updateEpic = epicMap.get(subtask.getEpicId());
        ArrayList<Integer> subtaskIds = updateEpic.getListSubtaskId();
        subtaskIds.add(subtask.getUniqueId());
        updateEpic.setListSubtaskId(subtaskIds);

        updateStatusEpic(updateEpic);
        return subtask.getUniqueId();
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getUniqueId();
        taskMap.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getUniqueId();
        epic.setListSubtaskId(epicMap.get(id).getListSubtaskId());
        epicMap.put(id, epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getUniqueId(), subtask);
        updateStatusEpic(epicMap.get(subtask.getEpicId()));
    }

    private void updateStatusEpic(Epic epic) {
        if (!epic.getListSubtaskId().isEmpty()) {
            int NEW = 0;
            int DONE = 0;
            int i = 0;
            for (Integer idList : epic.getListSubtaskId()) {
                if (i == NEW && subtaskMap.get(idList).getStatus().equals("NEW")) {
                    epic.setStatus("NEW");
                    NEW++;
                } else if (i == DONE && subtaskMap.get(idList).getStatus().equals("DONE")) {
                    epic.setStatus("DONE");
                    DONE++;
                } else {
                    epic.setStatus("IN_PROGRESS");
                }
                i++;
            }
        } else {
            epic.setStatus("NEW");
        }
    }

    @Override
    public void delIdTaskMap(int id) {
        taskMap.remove(id);
    }

    @Override
    public void delIdEpicMap(int id) {
        for (Integer idList : epicMap.get(id).getListSubtaskId()) {
            subtaskMap.remove(idList);
        }
        epicMap.remove(id);
    }

    @Override
    public void delIdSubtaskMap(int id) {
        Subtask subtask = subtaskMap.remove(id);
        if (subtask == null) {
            return;
        }

        Epic updateEpic = epicMap.get(subtask.getEpicId());
        ArrayList<Integer> listIds = updateEpic.getListSubtaskId();
        listIds.remove((Integer) id);

        updateStatusEpic(updateEpic);
    }

    @Override
    public ArrayList<Subtask> subtasksListToEpic(int id) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (!epicMap.get(id).getListSubtaskId().isEmpty()) {
            for (Integer key : epicMap.get(id).getListSubtaskId()) {
                subtasksList.add(subtaskMap.get(key));
            }
        }
        return subtasksList;
    }
}
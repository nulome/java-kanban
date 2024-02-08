package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    public HistoryManager inHistory = Managers.getDefaultHistory();
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private int newId = 0;

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HashMap<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public HashMap<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public void setInHistory(HistoryManager inHistory) {
        this.inHistory = inHistory;
    }

    public void setTaskMap(HashMap<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public void setEpicMap(HashMap<Integer, Epic> epicMap) {
        this.epicMap = epicMap;
    }

    public void setSubtaskMap(HashMap<Integer, Subtask> subtaskMap) {
        this.subtaskMap = subtaskMap;
    }

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
        for(Task task : taskMap.values()){
            inHistory.removeHistory(task.getUniqueId());
        }
        taskMap.clear();
    }

    @Override
    public void clearEpicMap() {
        for(Task task : epicMap.values()){
            inHistory.removeHistory(task.getUniqueId());
        }
        for(Task task : subtaskMap.values()){
            inHistory.removeHistory(task.getUniqueId());
        }
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void clearSubtaskMap() {
        for (Epic epic : epicMap.values()) {
            epic.setListSubtaskId(new ArrayList<>());
            updateStatusEpic(epic);
        }
        for(Task task : subtaskMap.values()){
            inHistory.removeHistory(task.getUniqueId());
        }
        subtaskMap.clear();
    }

    @Override
    public Task getTaskById(Integer key) {
        inHistory.addHistory(taskMap.get(key));
        return taskMap.get(key);
    }

    @Override
    public Epic getEpicById(Integer key) {
        inHistory.addHistory(epicMap.get(key));
        return epicMap.get(key);
    }

    @Override
    public Subtask getSubtaskById(Integer key) {
        inHistory.addHistory(subtaskMap.get(key));
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
                if (i == NEW && subtaskMap.get(idList).getStatus() == StatusTask.NEW) {
                    epic.setStatus(StatusTask.NEW);
                    NEW++;
                } else if (i == DONE && subtaskMap.get(idList).getStatus() == StatusTask.DONE) {
                    epic.setStatus(StatusTask.DONE);
                    DONE++;
                } else {
                    epic.setStatus(StatusTask.IN_PROGRESS);
                }
                i++;
            }
        } else {
            epic.setStatus(StatusTask.NEW);
        }
    }

    @Override
    public void delIdTaskMap(int id) {
        taskMap.remove(id);
        inHistory.removeHistory(id);
    }

    @Override
    public void delIdEpicMap(int id) {
        for (Integer idList : epicMap.get(id).getListSubtaskId()) {
            subtaskMap.remove(idList);
            inHistory.removeHistory(idList);
        }
        epicMap.remove(id);
        inHistory.removeHistory(id);
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
        inHistory.removeHistory(id);
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

    @Override
    public List<Task> getHistory() {
        return inHistory.getHistory();
    }

}
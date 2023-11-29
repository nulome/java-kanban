package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private int newId = 0;


    public ArrayList<Integer> gettingListTask() {
        ArrayList<Integer> listIds = new ArrayList<>();
        for (Task task : taskMap.values()) {
            listIds.add(task.getUniqueId());
        }
        return listIds;

    }

    public ArrayList<Integer> gettingListEpic() {
        ArrayList<Integer> listIds = new ArrayList<>();
        for (Epic epic : epicMap.values()) {
            listIds.add(epic.getUniqueId());
        }
        return listIds;
    }

    public ArrayList<Integer> gettingListSubtask() {
        ArrayList<Integer> listIds = new ArrayList<>();
        for (Subtask subtask : subtaskMap.values()) {
            listIds.add(subtask.getUniqueId());
        }
        return listIds;
    }

    public void clearTaskMap() {
        taskMap.clear();
    }

    public void clearEpicMap() {
        epicMap.clear();
    }

    public void clearSubtaskMap() {
        subtaskMap.clear();
    }

    public Task getTaskById(Integer key) {
        return taskMap.get(key);
    }

    public Epic getEpicById(Integer key) {
        return epicMap.get(key);
    }

    public Subtask getSubtaskById(Integer key) {
        return subtaskMap.get(key);
    }

    public Integer creationTask(Task task) {
        this.newId++;
        task.setUniqueId(newId);
        taskMap.put(task.getUniqueId(), task);
        return newId;
    }

    public Integer creationEpic(Epic epic) {
        this.newId++;
        epic.setUniqueId(newId);
        epicMap.put(epic.getUniqueId(), epic);
        return newId;
    }

    public Integer creationSubtask(Subtask subtask) {
        this.newId++;
        subtask.setUniqueId(newId);
        subtaskMap.put(subtask.getUniqueId(), subtask);

        ArrayList<Integer> list = epicMap.get(subtask.getEpicId()).getListSubtaskId();
        list.add(subtask.getUniqueId());
        epicMap.get(subtask.getEpicId()).setListSubtaskId(list);
        return subtask.getUniqueId();
    }

    public void updateTask(Task task) {
        int id = task.getUniqueId();
        taskMap.put(id, task);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getUniqueId();
        epic.setListSubtaskId(epicMap.get(id).getListSubtaskId());
        updateStatusEpic(epic);
        epicMap.put(id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getUniqueId(), subtask);
        updateStatusEpic(epicMap.get(subtask.getEpicId()));
    }

    private void updateStatusEpic(Epic epic){
        if(!epic.getListSubtaskId().isEmpty()) {
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
        }
    }

    public void delIdTaskMap(int id) {
        taskMap.remove(id);
    }

    public void delIdEpicMap(int id) {
        if(!epicMap.get(id).getListSubtaskId().isEmpty()){
            for (Integer idList : epicMap.get(id).getListSubtaskId()){
                subtaskMap.remove(idList);
            }
        }
        epicMap.remove(id);
    }

    public void delIdSubtaskMap(int id) {
        ArrayList<Integer> list = epicMap.get(subtaskMap.get(id).getEpicId()).getListSubtaskId();
        list.remove(id);
        epicMap.get(subtaskMap.get(id).getEpicId()).setListSubtaskId(list);
        subtaskMap.remove(id);
    }

    public ArrayList<Integer> showListEpicToId(int id) {
        return epicMap.get(id).getListSubtaskId();
    }
}

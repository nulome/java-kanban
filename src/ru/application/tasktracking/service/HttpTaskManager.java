package ru.application.tasktracking.service;

import com.google.gson.*;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.servers.HttpTaskServer;
import ru.application.tasktracking.servers.KVTaskClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private  Gson gson;
    KVTaskClient kvTaskClient;

    public HttpTaskManager(String url) {
        super(url); // URL к серверу KVServer  "http://localhost:8078/"
        kvTaskClient = new KVTaskClient(url);
        gson = new Gson();
    }


    @Override
    protected void save() {
        HashMap<String, String> data = new HashMap<>();

        for (Task task : getTaskMap().values()) {
            kvTaskClient.put(String.valueOf(task.getUniqueId()), gson.toJson(task));
            data.put(String.valueOf(task.getUniqueId()), "t");
        }
        for (Epic task : getEpicMap().values()) {
            kvTaskClient.put(String.valueOf(task.getUniqueId()), gson.toJson(task));
            data.put(String.valueOf(task.getUniqueId()), "e");
        }
        for (Subtask task : getSubtaskMap().values()) {
            kvTaskClient.put(String.valueOf(task.getUniqueId()), gson.toJson(task));
            data.put(String.valueOf(task.getUniqueId()), "s");
        }

        if (!getHistory().isEmpty()) {
            kvTaskClient.put("history", gson.toJson(historyToList()));
            data.put("history", "history");
        }

        if (!data.isEmpty()) {
            kvTaskClient.put("data", gson.toJson(data));
            data.put("data", "data");
        }
        loadFromServer();
    }


    protected void loadFromServer() {
        String data = kvTaskClient.load("data");
        //System.out.println("data: " + data);
        //String historyStr;
        HashMap<String, String> datats = gson.fromJson(data, HashMap.class);
        //System.out.println("HashMap" + datats);

        int saveMaxId = 0;

        for(String key : datats.keySet()){
            int idMax = Integer.parseInt(key);
            if (idMax > saveMaxId) {
                saveMaxId = idMax;
            }

            if(datats.get(key).equals("t")){
                String taskStr = kvTaskClient.load(key);
                //System.out.println("str task: " + taskStr);
                Task task = gson.fromJson(taskStr, Task.class);
                //System.out.println(task);
                //System.out.println(task.getUniqueId());

                taskMap.put(task.getUniqueId(), task); //++
            }
            if (datats.get(key).equals("e")){
                String epicStr = kvTaskClient.load(key);
                //System.out.println("str epic: " + epicStr);
                Epic epic = gson.fromJson(epicStr, Epic.class);
                //System.out.println(epic);
                //System.out.println(epic.getUniqueId());

                epicMap.put(epic.getUniqueId(), epic); //++
            }
            if (datats.get(key).equals("history")){
                String historyStr = kvTaskClient.load(key);
                List<Integer> listHistory = gson.fromJson(historyStr, ArrayList.class);
                loadHistoryToList(listHistory);
                //System.out.println(listHistory);
            }
        }
        for(String key : datats.keySet()){
            if(datats.get(key).equals("s")){
                String subtaskStr = kvTaskClient.load(key);
                //System.out.println("str subtask: " + subtaskStr);
                Subtask subtask = gson.fromJson(subtaskStr, Subtask.class);
                //System.out.println(subtask);
                //System.out.println(subtask.getUniqueId());

                subtaskMap.put(subtask.getUniqueId(), subtask); //++
            }
        }

        newId = saveMaxId;


    }

    private void loadHistoryToList(List<Integer> listHistory) {
        for (Integer taskId : listHistory) {
            Task task;
            if (taskMap.containsKey(taskId)) {
                task = taskMap.get(taskId);
            } else if (epicMap.containsKey(taskId)) {
                task = epicMap.get(taskId);
            } else {
                task = subtaskMap.get(taskId);
            }

            inHistory.addHistory(task);
        }
    }
    private List<Integer> historyToList() {
        List<Integer> listHistory = new ArrayList<>();
        for (Task task : getHistory()) {
            listHistory.add(task.getUniqueId());
        }
        return listHistory;
    }
}

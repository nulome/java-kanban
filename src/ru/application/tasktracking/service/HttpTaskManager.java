package ru.application.tasktracking.service;

import com.google.gson.*;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.servers.HttpTaskServer;
import ru.application.tasktracking.servers.KVTaskClient;

import java.util.HashMap;

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
        int intgj = 9;
        for (Task task : getTaskMap().values()) {
            kvTaskClient.put(String.valueOf(task.getUniqueId()), gson.toJson(task)); // String key, String json
            data.put(String.valueOf(task.getUniqueId()), "t");
        }
        for (Epic task : getEpicMap().values()) {
            kvTaskClient.put(String.valueOf(task.getUniqueId()), gson.toJson(task)); // String key, String json
            data.put(String.valueOf(task.getUniqueId()), "e");
        }
        for (Subtask task : getSubtaskMap().values()) {
            kvTaskClient.put(String.valueOf(task.getUniqueId()), gson.toJson(task)); // String key, String json
            data.put(String.valueOf(task.getUniqueId()), "s");
        }

        if (!getHistory().isEmpty()) {
            kvTaskClient.put("history", gson.toJson(getHistory())); // String key, String json
            data.put("history", "history");
        }

        if (data.isEmpty()) {
            kvTaskClient.put("data", gson.toJson(data)); // String key, String json
            data.put("data", "data");
        }

    }


    protected void loadFromServer() {
        String data = kvTaskClient.load("data");
        System.out.println(data);
        //HashMap<String, String> data = gson.fromJson(data, HashMap.class);

    }

}

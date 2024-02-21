package ru.application.tasktracking.servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.service.Managers;
import ru.application.tasktracking.service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final Gson gson;
    private final TaskManager taskManager;

    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {

        String file = "http://localhost:8078/";
        this.gson = new Gson();
        this.taskManager = Managers.getHttpTasksManager(file);
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
    }


   /* public static void main(String[] args) throws IOException {
        new KVServer().start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();

        *//*FileBackedTasksManager fw = new FileBackedTasksManager("src/ru/application/tasktracking/resources/Tasks.csv");
        Integer checkEpic = fw.creationEpic(new Epic("TestNewEpic", "TestDescription", StatusTask.NEW));
        Subtask checkSubtask = new Subtask("TestNewSubtask", "TestDescription", StatusTask.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 2, 0), checkEpic);

        Integer integ = fw.creationSubtask(checkSubtask);
        Subtask sub = fw.getSubtaskById(integ);
        Gson gss = new Gson();
        String stree = gss.toJson(sub);
        System.out.println(stree);*//*
    }*/

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String method = httpExchange.getRequestMethod();
                String uri = httpExchange.getRequestURI().getPath();
                //System.out.println(uri); // "/tasks"
                String parameter = httpExchange.getRequestURI().getQuery();
                //System.out.println(parameter); // "/tasks"

                String response = "";
                if (method.equals("GET")) {  // switch внутри switch почему то не корретно работает.
                    switch (uri) {
                        case "/tasks/history":
                            response = getHistory();
                            break;
                        case "/tasks/":
                            response = getPrioritizedTasks();
                            break;
                        case "/tasks/subtask/epic":
                            String epicId = parameter.replaceFirst("id=", "");
                            response = subtasksListToEpic(epicId);
                            break;
                        case "/tasks/task/":
                            if (parameter == null) {
                                response = getTasks();
                            } else {
                                String id = parameter.replaceFirst("id=", "");
                                response = getTaskById(id);
                            }
                            break;
                        case "/tasks/subtask/":
                            if (parameter == null) {
                                response = getSubtasks();
                            } else {
                                String id = parameter.replaceFirst("id=", "");
                                response = getSubtaskById(id);
                            }
                            break;
                        case "/tasks/epic/":
                            if (parameter == null) {
                                response = getEpics();
                            } else {
                                String id = parameter.replaceFirst("id=", "");
                                response = getEpicById(id);
                            }
                            break;
                        /*case "/tasks/save/":
                            response = "";

                            break;
                        case "/tasks/load/":
                            response = "";

                            break; */
                    }


                } else if (method.equals("POST")) {
                    switch (uri) {
                        case "/tasks/task/":
                            Task task = jsonToTask(readText(httpExchange));
                            if (task.getUniqueId() == 0) {
                                creationTask(task);
                            } else {
                                updateTask(task);
                            }
                            response = "Задача успешно добавлена/обнолвена.";
                            break;
                        case "/tasks/epic/":
                            Epic epic = jsonToEpic(readText(httpExchange));
                            if (epic.getUniqueId() == 0) {
                                creationEpic(epic);
                            } else {
                                updateEpic(epic);
                            }
                            response = "Задача успешно добавлена/обнолвена.";
                            break;
                        case "/tasks/subtask/":
                            Subtask subtask = jsonToSubtask(readText(httpExchange));
                            if (subtask.getUniqueId() == 0) {
                                creationSubtask(subtask);
                            } else {
                                updateSubtask(subtask);
                            }
                            response = "Задача успешно добавлена/обнолвена.";
                            break;

                    }
                } else if (method.equals("DELETE")) {
                    switch (uri) {
                        case "/tasks/task/":
                            if (parameter == null) {
                                clearTaskMap();
                            } else {
                                String id = parameter.replaceFirst("id=", "");
                                delIdTaskMap(parseString(id));
                            }
                            response = "Удаление выполнено успешно.";
                            break;
                        case "/tasks/epic/":
                            if (parameter == null) {
                                clearEpicMap();
                            } else {
                                String id = parameter.replaceFirst("id=", "");
                                delIdEpicMap(parseString(id));
                            }
                            response = "Удаление выполнено успешно.";
                            break;
                        case "/tasks/subtask/":
                            if (parameter == null) {
                                clearSubtaskMap();
                            } else {
                                String id = parameter.replaceFirst("id=", "");
                                delIdSubtaskMap(parseString(id));
                            }
                            response = "Удаление выполнено успешно.";
                            break;

                    }


                }

                if (response.isBlank()) {
                    httpExchange.sendResponseHeaders(400, 0);
                    response = "Вы использовали какой-то другой метод!";
                } else {
                    httpExchange.sendResponseHeaders(200, 0);
                }


                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    private void delIdTaskMap(int id) {
        taskManager.delIdTaskMap(id);
    }

    private void delIdEpicMap(int id) {
        taskManager.delIdEpicMap(id);
    }

    private void delIdSubtaskMap(int id) {
        taskManager.delIdSubtaskMap(id);
    }

    private void clearTaskMap() {
        taskManager.clearTaskMap();
    }

    private void clearEpicMap() {
        taskManager.clearEpicMap();
    }

    private void clearSubtaskMap() {
        taskManager.clearSubtaskMap();
    }

    private Task jsonToTask(String task) {
        return gson.fromJson(task, Task.class);
    }

    private Epic jsonToEpic(String task) {
        return gson.fromJson(task, Epic.class);
    }

    private Subtask jsonToSubtask(String task) {
        return gson.fromJson(task, Subtask.class);
    }

    private void creationTask(Task task) {
        taskManager.creationTask(task);
    }

    private void creationEpic(Epic epic) {
        taskManager.creationEpic(epic);
    }

    private void creationSubtask(Subtask subtask) {
        taskManager.creationSubtask(subtask);
    }

    private void updateTask(Task task) {
        taskManager.updateTask(task);
    }

    private void updateEpic(Epic epic) {
        taskManager.updateEpic(epic);
    }

    private void updateSubtask(Subtask subtask) {
        taskManager.updateSubtask(subtask);
    }

    private String getTaskById(String id) {
        return gson.toJson(taskManager.getTaskById(parseString(id)));
    }

    private String getEpicById(String id) {
        return gson.toJson(taskManager.getEpicById(parseString(id)));
    }

    private String getSubtaskById(String id) {
        return gson.toJson(taskManager.getSubtaskById(parseString(id)));
    }

    private String subtasksListToEpic(String id) {
        return gson.toJson(taskManager.subtasksListToEpic(parseString(id)));
    }

    private int parseString(String id) {
        return Integer.parseInt(id);
    }

    private String getHistory() {
        return gson.toJson(taskManager.getHistory());
    }

    private String getPrioritizedTasks() {
        return gson.toJson(taskManager.getPrioritizedTasks());
    }

    private String getTasks() {
        return gson.toJson(taskManager.getTasks());
    }

    private String getEpics() {
        return gson.toJson(taskManager.getEpics());
    }

    private String getSubtasks() {
        return gson.toJson(taskManager.getSubtasks());
    }


    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);

    }

    private static String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private static void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }


}

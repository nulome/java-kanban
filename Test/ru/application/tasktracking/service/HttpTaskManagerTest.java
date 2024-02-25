package ru.application.tasktracking.service;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.servers.HttpTaskServer;
import ru.application.tasktracking.servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_BAD_METHOD;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private final String urlServer = "http://localhost:8078/";
    HttpTaskServer httpTaskServer;
    Gson gson = new Gson();

    @BeforeAll
    public static void start() throws IOException {
        new KVServer().start();
    }


    @BeforeEach
    public void createManager() throws IOException {
        manager = new HttpTaskManager(urlServer, false);
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }



    @Test
    void testEndpoint() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasksslo");
        HttpResponse<String> response = requestGet(uri);

        System.out.println("Тело ответа: " + response.body());
        System.out.println("Код ответа: " + response.statusCode());

        assertEquals(HTTP_BAD_METHOD, response.statusCode(), "Неверная обрботка не подтверждена");

        uri = URI.create("http://localhost:8080/tasks/");
        response = requestGet(uri);
        assertEquals(HTTP_OK, response.statusCode(), "Верная обработка с ошибкой");
        assertEquals("[]", response.body(), "Данные на сервере не пусты");

        taskEmptyNEW = new Task("Test", "TestDescription", StatusTask.NEW);
        epicEmptyNEW = new Epic("Test", "TestDescription", StatusTask.NEW);
        subtaskNEWToEpicEmpty = new Subtask("Test", "TestDescription", StatusTask.NEW, 2);


        String gsonTaskStr = gson.toJson(taskEmptyNEW);
        uri = URI.create("http://localhost:8080/tasks/task/");
        response = requestPost(uri, gsonTaskStr);
        assertEquals(HTTP_OK, response.statusCode(), "Верная обработка с ошибкой");

        gsonTaskStr = gson.toJson(epicEmptyNEW);
        uri = URI.create("http://localhost:8080/tasks/epic/");
        response = requestPost(uri, gsonTaskStr);
        assertEquals(HTTP_OK, response.statusCode(), "Верная обработка с ошибкой");

        gsonTaskStr = gson.toJson(subtaskNEWToEpicEmpty);
        uri = URI.create("http://localhost:8080/tasks/subtask/");
        response = requestPost(uri, gsonTaskStr);
        assertEquals(HTTP_OK, response.statusCode(), "Верная обработка с ошибкой");

        gsonTaskStr = gson.toJson(new Subtask("Test", "TestDescription", StatusTask.NEW, 3,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 1, 1, 2, 0),2));
        uri = URI.create("http://localhost:8080/tasks/subtask/");
        response = requestPost(uri, gsonTaskStr);
        assertEquals(HTTP_OK, response.statusCode(), "Обновление задачи с ошибкой");


        uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        response = requestGet(uri);
        assertEquals(HTTP_OK, response.statusCode(), "Получение задачи по id с ошибкой");
        String bodyTaskStr = response.body();
        taskEmptyNEW.setUniqueId(1);
        gsonTaskStr = gson.toJson(taskEmptyNEW);
        assertEquals(bodyTaskStr, gsonTaskStr, "Вернувшийся gson не совпадает");

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        response = requestGet(uri);
        assertEquals(HTTP_OK, response.statusCode(), "Получение задачи по id с ошибкой");

        uri = URI.create("http://localhost:8080/tasks/subtask/epic?id=2");
        response = requestGet(uri);
        assertEquals(HTTP_OK, response.statusCode(), "Получение подзадач от эпика по id с ошибкой");
        bodyTaskStr = response.body();
        ArrayList listBody = gson.fromJson(bodyTaskStr, ArrayList.class);
        // есть способ перевода листа объектов из gson сразу в объекты?
        // Так как добавлены скобочки [] он не корректно считывал.

        bodyTaskStr = gson.toJson(listBody.get(0));
        Subtask subtaskBody = gson.fromJson(bodyTaskStr, Subtask.class);
        Integer uniqId = subtaskBody.getUniqueId();
        assertEquals(3, uniqId, "Подзадача от эпика не верная.");


        uri = URI.create("http://localhost:8080/tasks/history");
        response = requestGet(uri);
        bodyTaskStr = response.body();
        System.out.println(bodyTaskStr);
        listBody = gson.fromJson(bodyTaskStr, ArrayList.class);
        assertEquals(2, listBody.size(), "Количество просмотров разное.");

        uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        requestDelete(uri);
        uri = URI.create("http://localhost:8080/tasks/task/");
        response = requestGet(uri);
        assertEquals("[]", response.body(), "Данные на сервере не пусты");

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        requestDelete(uri);
        uri = URI.create("http://localhost:8080/tasks/subtask/");
        response = requestGet(uri);
        assertEquals("[]", response.body(), "Данные на сервере не пусты");

    }


    private HttpResponse<String> requestGet(URI uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return  response;
    }
    private HttpResponse<String> requestPost(URI uri, String gson) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return  response;
    }
    private HttpResponse<String> requestDelete(URI uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return  response;
    }
    @AfterEach
    public void httpStop() {
        httpTaskServer.stop();
    }

    /*@AfterAll
    public static void stop() throws IOException { // java.net.BindException: Address already in use: bind
        new KVServer().stop();
    }*/

}
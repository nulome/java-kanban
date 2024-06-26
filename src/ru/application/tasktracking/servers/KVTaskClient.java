package ru.application.tasktracking.servers;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String url; // "http://localhost:8078/"
    private final String API_TOKEN;

    public KVTaskClient(String url) {
        this.url = url;
        API_TOKEN = register();
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }

    private String register() {
        URI uri = URI.create(url + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            System.out.println("Успешно зарегистрировались");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }


}

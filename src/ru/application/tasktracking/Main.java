package ru.application.tasktracking;


import ru.application.tasktracking.servers.HttpTaskServer;
import ru.application.tasktracking.servers.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /**
        TODO Привет, Патимат!


*/

        new KVServer().start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();

    }
}
package ru.application.tasktracking;


import ru.application.tasktracking.objects.Task;
import ru.application.tasktracking.servers.HttpTaskServer;
import ru.application.tasktracking.service.HttpTaskManager;
import ru.application.tasktracking.servers.KVServer;
import ru.application.tasktracking.service.StatusTask;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /**
        Привет, Патимат!
*/

        new KVServer().start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();





    }
}
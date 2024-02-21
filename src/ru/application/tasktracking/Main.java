package ru.application.tasktracking;


import ru.application.tasktracking.servers.HttpTaskServer;
import ru.application.tasktracking.servers.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /**
        Привет, Патимат!

         - пока что не почистил комментарии.
         - не совсем понял, как проверять загрузку с сервера в тестах, если бы уже были данные на нем.
*/

        new KVServer().start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();

    }
}
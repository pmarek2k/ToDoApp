package efs.task.todoapp;

import com.sun.net.httpserver.HttpServer;
import efs.task.todoapp.web.WebServerFactory;

import org.apache.log4j.Logger;

public class ToDoApplication {
    private static final Logger LOGGER = Logger.getLogger(ToDoApplication.class);

    public static void main(String[] args) {
        var application = new ToDoApplication();
        var server = application.createServer();
        server.start();

        LOGGER.info("ToDoApplication's server started ...");
    }

    public HttpServer createServer() {
        return WebServerFactory.createServer();
    }
}

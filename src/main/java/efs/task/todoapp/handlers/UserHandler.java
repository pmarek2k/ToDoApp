package efs.task.todoapp.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import efs.task.todoapp.ToDoApplication;
import efs.task.todoapp.httpMethods.UserPostMethod;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.utils.BadRequestException;

import java.io.IOException;
import org.apache.log4j.Logger;

public class UserHandler implements HttpHandler {

    private ToDoService service;
    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

    public UserHandler(ToDoService service){
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            LOGGER.info("Handling user post method");
            UserPostMethod.Post(exchange, service);
        }
        catch(BadRequestException e){
            LOGGER.error("ERROR: " + e.getResponseCode() + " " + e.getMessage());
            exchange.sendResponseHeaders(e.getResponseCode(), 0);
            exchange.close();
        }
        catch(Exception e){
            LOGGER.error("Exception occured!");
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
            e.printStackTrace();
        }
    }


}

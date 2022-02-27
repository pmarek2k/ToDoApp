package efs.task.todoapp.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import efs.task.todoapp.httpMethods.DeleteMethod;
import efs.task.todoapp.httpMethods.GetMethod;
import efs.task.todoapp.httpMethods.PutMethod;
import efs.task.todoapp.httpMethods.TaskPostMethod;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.utils.BadRequestException;

import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;

public class TaskHandler implements HttpHandler {

    private ToDoService service;
    private static final Logger LOGGER = Logger.getLogger(TaskHandler.class);

    public TaskHandler(ToDoService service) {

        this.service = service;
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            final List<String> headerValues = exchange.getRequestHeaders().get("auth");
            if (headerValues == null) {
                throw new BadRequestException("Request doesn't contain auth header", 400);
            }
            if (!(service.isAuthCorrect(headerValues.get(0)))) {
                throw new BadRequestException("No user or password is incorrect", 401);
            }

            switch(exchange.getRequestMethod()){
                case "POST":
                    LOGGER.info("Handling task post method.");
                    TaskPostMethod.Post(exchange, service);
                    break;
                case "GET":
                    LOGGER.info("Handling task get method.");
                    GetMethod.Get(exchange, service);
                    break;
                case "PUT":
                    LOGGER.info("Handling task put method.");
                    PutMethod.Put(exchange, service);
                    break;
                case "DELETE":
                    LOGGER.info("Handling task delete method.");
                    DeleteMethod.Delete(exchange, service);
                    break;
                default:
                    break;
            }
        } catch (BadRequestException e) {
            LOGGER.error("ERROR: " + e.getResponseCode() + " " + e.getMessage());
            exchange.sendResponseHeaders(e.getResponseCode(), 0);
            exchange.close();
        }
        catch (Exception e){
            LOGGER.error("Exception occured!");
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
            e.printStackTrace();
        }

    }
}


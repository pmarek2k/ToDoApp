package efs.task.todoapp.httpMethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.handlers.TaskHandler;
import efs.task.todoapp.repository.TaskEntity;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.utils.BadRequestException;

import java.io.IOException;
import org.apache.log4j.Logger;

public class DeleteMethod {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(DeleteMethod.class);

    public static void Delete(HttpExchange exchange, ToDoService service) throws IOException, BadRequestException {

        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        String id = pathSplit[3];

        LOGGER.info("Delete request: auth: " + exchange.getRequestHeaders().get("auth").get(0) + ", id: " + id);

        service.deleteTask(exchange.getRequestHeaders().get("auth").get(0), id);

        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
}

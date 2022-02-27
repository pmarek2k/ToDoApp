package efs.task.todoapp.httpMethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.repository.TaskEntity;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.utils.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class GetMethod {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(GetMethod.class);

    public static void Get(HttpExchange exchange, ToDoService service) throws IOException, BadRequestException {

        if(exchange.getRequestURI().getPath().equals("/todo/task")){
            LOGGER.info("Get request: auth: " + exchange.getRequestHeaders().get("auth").get(0));
            String auth = exchange.getRequestHeaders().get("auth").get(0);
            List<TaskEntity> taskList = service.getAllTasks(auth);

            String response = objectMapper.writeValueAsString(taskList);

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        else{
            String[] pathSplit = exchange.getRequestURI().getPath().split("/");
            String id = pathSplit[3];

            LOGGER.info("Get request: auth: " + exchange.getRequestHeaders().get("auth").get(0) + ", id: " + id);

            TaskEntity task = service.getTask(exchange.getRequestHeaders().get("auth").get(0), id);
            String response = objectMapper.writeValueAsString(task);

            LOGGER.info(objectMapper.writeValueAsString(task));

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        exchange.close();
    }
}

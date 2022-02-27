package efs.task.todoapp.httpMethods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.repository.TaskEntity;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.utils.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class PutMethod {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(PutMethod.class);

    public static void Put(HttpExchange exchange, ToDoService service) throws IOException, BadRequestException, ParseException {

        InputStream requestBodyStream = exchange.getRequestBody();
        final String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

        TaskEntity task = objectMapper.readValue(requestBody, TaskEntity.class);
        task.setAuth(exchange.getRequestHeaders().get("auth").get(0));

        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        String id = pathSplit[3];

        LOGGER.info("Put request: auth: " + exchange.getRequestHeaders().get("auth").get(0) + ", id: " + id);

        LOGGER.info("Task before modification:" + objectMapper.writeValueAsString(service.getTask(exchange.getRequestHeaders().get("auth").get(0), id)));

        service.modifyTask(exchange.getRequestHeaders().get("auth").get(0), id, task);

        String response = objectMapper.writeValueAsString(service.getTask(exchange.getRequestHeaders().get("auth").get(0), id));
        LOGGER.info("Task after modification:" + objectMapper.writeValueAsString(service.getTask(exchange.getRequestHeaders().get("auth").get(0), id)));

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        exchange.close();
    }
}

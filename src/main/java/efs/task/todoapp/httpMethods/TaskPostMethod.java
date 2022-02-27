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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class TaskPostMethod {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(TaskPostMethod.class);

    public static void Post(HttpExchange exchange, ToDoService service) throws IOException, BadRequestException {
        InputStream requestBodyStream = exchange.getRequestBody();
        final String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(df);

        //TODO: 201 - sukces, 400 - brak tresci
        TaskEntity task = objectMapper.readValue(requestBody, TaskEntity.class);
        task.setAuth(exchange.getRequestHeaders().get("auth").get(0));

        LOGGER.info("Task post request: auth: " + exchange.getRequestHeaders().get("auth").get(0));

        if(task.getDescription() == null){
            throw new BadRequestException("Wrong body data", 400);
        }

        service.addTask(task);

        String response = objectMapper.writeValueAsString(task.getID());

        exchange.sendResponseHeaders(201, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        exchange.close();
    }
}

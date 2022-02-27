package efs.task.todoapp.httpMethods;

import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.repository.UserEntity;
import efs.task.todoapp.service.ToDoService;
import efs.task.todoapp.utils.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.log4j.Logger;

public class UserPostMethod {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(UserPostMethod.class);

    public static void Post(HttpExchange exchange, ToDoService service) throws IOException, BadRequestException {
        InputStream requestBodyStream = exchange.getRequestBody();
        final String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

        LOGGER.info("User Post request");
        LOGGER.info("Request body: " + requestBody);
            UserEntity user = objectMapper.readValue(requestBody, UserEntity.class);
            if(user.getUsername() == null || user.getPassword() == null || user.getUsername().equals("") || user.getPassword().equals("")){
                throw new BadRequestException("Wrong body data", 400);
            }
            service.addUser(user);

            exchange.sendResponseHeaders(201, 0);
            exchange.close();

    }
}

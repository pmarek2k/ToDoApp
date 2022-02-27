package efs.task.todoapp;

import efs.task.todoapp.util.ToDoServerExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ToDoServerExtension.class)
class UserPostTest {

    public static final int USER_ADDED = 201;
    public static final int INCORRECT_REQUEST = 400;
    public static final int USER_ALREADY_EXISTS = 409;
    public static final String TODO_APP_PATH = "http://localhost:8080/todo/";
    public static final String CORRECT_REQUEST_BODY = "{\n" +
            "  \"username\": \"janKowalski\",\n" +
            "  \"password\": \"am!sK#123\"\n" +
            "}";
    public static final String INCORRECT_REQUEST_BODY = "{\n" +
            "  \"password\": \"am!sK#123\"\n" +
            "}";

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @Timeout(1)
    void shouldReturnUserAddedStatusForCorrectRequestBody() throws IOException, InterruptedException {
        //given
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(CORRECT_REQUEST_BODY))
                .build();

        //when
        var httpResponse = httpClient.send(httpRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(USER_ADDED);
    }

    @Test
    @Timeout(1)
    void shouldReturnIncorrectRequestBodyStatusForIncorrectRequestBody() throws IOException, InterruptedException {
        //given
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(INCORRECT_REQUEST_BODY))
                .build();

        //when
        var httpResponse = httpClient.send(httpRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(INCORRECT_REQUEST);
    }

    @Test
    @Timeout(1)
    void shouldReturnUserAlreadyExistsStatusForSameUser() throws IOException, InterruptedException {
        //given
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(CORRECT_REQUEST_BODY))
                .build();

        //when
        var httpResponse1 = httpClient.send(httpRequest, ofString());
        var httpResponse2 = httpClient.send(httpRequest, ofString());

        //then
        assertThat(httpResponse2.statusCode()).as("Response status code").isEqualTo(USER_ALREADY_EXISTS);
    }
}
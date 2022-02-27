package efs.task.todoapp;

import efs.task.todoapp.util.Base64Utils;
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
class TaskPostTest {

    public static final int TASK_ADDED = 201;
    public static final int INCORRECT_REQUEST = 400;
    public static final int NO_USER_OR_INCORRECT_PASSWORD = 401;

    public static final String TODO_APP_PATH = "http://localhost:8080/todo/";
    public static final String AUTH = Base64Utils.encode("janKowalski") + ":" + Base64Utils.encode("am!sK#123");
    public static final String CORRECT_REQUEST_BODY = "{\n" +
            "  \"description\": \"Kup mleko\",\n" +
            "  \"due\": \"2021-06-30\"\n" +
            "}";
    public static final String INCORRECT_REQUEST_BODY = "{\n" +
            "  \"descriapfption\": \"Kup mleko\"\n" +
            "}";
    public static final String USER_REQUEST_BODY = "{\n" +
            "  \"username\": \"janKowalski\",\n" +
            "  \"password\": \"am!sK#123\"\n" +
            "}";

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @Timeout(1)
    void shouldReturnTaskAddedStatusForCorrectRequestBody() throws IOException, InterruptedException {
        //given
        var httpTaskRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(CORRECT_REQUEST_BODY))
                .header("auth", AUTH)
                .build();

        var httpUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(USER_REQUEST_BODY))
                .build();

        //when
        httpClient.send(httpUserRequest, ofString());
        var httpResponse = httpClient.send(httpTaskRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(TASK_ADDED);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoHeaderOrBodyForIncorrectBody() throws IOException, InterruptedException {
        //given
        var httpTaskRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(INCORRECT_REQUEST_BODY))
                .header("auth", AUTH)
                .build();

        var httpUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(USER_REQUEST_BODY))
                .build();

        //when
        httpClient.send(httpUserRequest, ofString());
        var httpResponse = httpClient.send(httpTaskRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(INCORRECT_REQUEST);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoHeaderOrBodyForNoHeader() throws IOException, InterruptedException {
        //given
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(CORRECT_REQUEST_BODY))
                .build();

        //when
        var httpResponse = httpClient.send(httpRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(INCORRECT_REQUEST);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoUserOrIncorrectPasswordStatusForWrongHeader() throws IOException, InterruptedException {
        //given
        var httpTaskRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(CORRECT_REQUEST_BODY))
                .header("auth", "testHeader:2032pll")
                .build();

        var httpUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(USER_REQUEST_BODY))
                .build();

        //when
        httpClient.send(httpUserRequest, ofString());
        var httpResponse = httpClient.send(httpTaskRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(NO_USER_OR_INCORRECT_PASSWORD);
    }
}
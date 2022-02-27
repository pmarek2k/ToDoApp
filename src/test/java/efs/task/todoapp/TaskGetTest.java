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
import java.util.Base64;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ToDoServerExtension.class)
class TaskGetTest {

    public static final int TASK_INFO = 200;
    public static final int NO_HEADER = 400;
    public static final int NO_USER_OR_INCORRECT_PASSWORD = 401;
    public static final int TASK_BELONGS_TO_ANOTHER_USER = 403;
    public static final int NO_SUCH_TASK = 404;

    public static final String INCORRECT_UUID = "0afaa7c2-10dd-4ea8-9c12-7917cf91f222";

    public static final String TODO_APP_PATH = "http://localhost:8080/todo/";
    public static final String AUTH = Base64Utils.encode("janKowalski") + ":" + Base64Utils.encode("am!sK#123");
    public static final String AUTH_2 = Base64Utils.encode("krzysztof321") + ":" + Base64Utils.encode("password");
    public static final String CORRECT_REQUEST_BODY = "{\n" +
            "  \"description\": \"Kup mleko\",\n" +
            "  \"due\": \"2021-06-30\"\n" +
            "}";
    public static final String INCORRECT_REQUEST_BODY = "{\n" +
            "  \"description\": \"Kup mleko\"\n" +
            "}";
    public static final String USER_REQUEST_BODY = "{\n" +
            "  \"username\": \"janKowalski\",\n" +
            "  \"password\": \"am!sK#123\"\n" +
            "}";
    public static final String USER_REQUEST_BODY_2 = "{\n" +
            "  \"username\": \"krzysztof321\",\n" +
            "  \"password\": \"password\"\n" +
            "}";

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @Timeout(1)
    void shouldReturnTaskInfoStatusCodeForCorrectRequest() throws IOException, InterruptedException {
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

        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .GET()
                .header("auth", AUTH)
                .build();

        //when
        httpClient.send(httpUserRequest, ofString());
        httpClient.send(httpTaskRequest, ofString());
        var httpResponse = httpClient.send(httpGetRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(TASK_INFO);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoHeaderStatusCodeForNoAuthHeader() throws IOException, InterruptedException {
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

        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .GET()
                .build();

        //when
        httpClient.send(httpUserRequest, ofString());
        httpClient.send(httpTaskRequest, ofString());
        var httpResponse = httpClient.send(httpGetRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(NO_HEADER);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoUserOrPasswordIncorrectStatusCodeForIncorrectAuth() throws IOException, InterruptedException {
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

        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .GET()
                .header("auth", "padpgpag:kadgkke")
                .build();

        //when
        httpClient.send(httpUserRequest, ofString());
        httpClient.send(httpTaskRequest, ofString());
        var httpResponse = httpClient.send(httpGetRequest, ofString());

        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(NO_USER_OR_INCORRECT_PASSWORD);
    }

    //TASK INFO TESTS

    @Test
    @Timeout(1)
    void shouldReturnTaskInfoForCorrectTaskId() throws IOException, InterruptedException {
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

        httpClient.send(httpUserRequest, ofString());
        var httpTaskResponse = httpClient.send(httpTaskRequest, ofString());
        String[] response = httpTaskResponse.body().split("\"");
        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task/" + response[3]))
                .GET()
                .header("auth", AUTH)
                .build();

        //when
        var httpResponse = httpClient.send(httpGetRequest, ofString());


        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(TASK_INFO);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoHeaderStatusCodeForNoHeader() throws IOException, InterruptedException {
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

        httpClient.send(httpUserRequest, ofString());
        var httpTaskResponse = httpClient.send(httpTaskRequest, ofString());
        String[] response = httpTaskResponse.body().split("\"");
        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task/" + response[3]))
                .GET()
                .build();

        //when
        var httpResponse = httpClient.send(httpGetRequest, ofString());


        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(NO_HEADER);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoUserOrPasswordIncorrectStatusCodeForCorrectIdAndIncorrectAuth() throws IOException, InterruptedException {
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

        httpClient.send(httpUserRequest, ofString());
        var httpTaskResponse = httpClient.send(httpTaskRequest, ofString());
        String[] response = httpTaskResponse.body().split("\"");
        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task/" + response[3]))
                .GET()
                .header("auth", "kadffkadfk:ksfksh")
                .build();

        //when
        var httpResponse = httpClient.send(httpGetRequest, ofString());


        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(NO_USER_OR_INCORRECT_PASSWORD);
    }

    @Test
    @Timeout(1)
    void shouldReturnTaskBelongsToAnotherUserStatusCodeForIncorrectAuth() throws IOException, InterruptedException {
        //given
        var httpTaskRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(CORRECT_REQUEST_BODY))
                .header("auth", AUTH)
                .build();

        var httpUser1Request = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(USER_REQUEST_BODY))
                .build();

        var httpUser2Request = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "user"))
                .POST(HttpRequest.BodyPublishers.ofString(USER_REQUEST_BODY_2))
                .build();

        httpClient.send(httpUser1Request, ofString());
        httpClient.send(httpUser2Request, ofString());
        var httpTaskResponse = httpClient.send(httpTaskRequest, ofString());
        String[] response = httpTaskResponse.body().split("\"");
        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task/" + response[3]))
                .GET()
                .header("auth", AUTH_2)
                .build();

        //when
        var httpResponse = httpClient.send(httpGetRequest, ofString());


        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(TASK_BELONGS_TO_ANOTHER_USER);
    }

    @Test
    @Timeout(1)
    void shouldReturnNoSuchTaskStatusCodeForIncorrectTaskId() throws IOException, InterruptedException {
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


        var httpGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(TODO_APP_PATH + "task/" + INCORRECT_UUID))
                .GET()
                .header("auth", AUTH)
                .build();


        //when
        httpClient.send(httpUserRequest, ofString());
        httpClient.send(httpTaskRequest, ofString());
        var httpResponse = httpClient.send(httpGetRequest, ofString());


        //then
        assertThat(httpResponse.statusCode()).as("Response status code").isEqualTo(NO_SUCH_TASK);
    }
}
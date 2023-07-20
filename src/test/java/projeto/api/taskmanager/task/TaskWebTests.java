package projeto.api.taskmanager.task;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.user.User;
import projeto.api.taskmanager.user.dtos.LoginDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class TaskWebTests {
    
    @Autowired
    private WebTestClient webClient;

    private CommonResponse<String> token;

    @BeforeAll
    private void setup() {
        User user = new User("test name", "test@email.com", "123");
        LoginDTO loginDTO = new LoginDTO(user.getEmail(), user.getPassword());
        
        webClient.post()
                .uri("/users/")
                .bodyValue(user)
                .exchange();
        
         token = webClient.post()
                .uri("/users/login")
                .bodyValue(loginDTO)
                .exchange()
                .expectBody(new ParameterizedTypeReference<CommonResponse<String>>() {})
                .returnResult()
                .getResponseBody();
    }

    @Test
    public void createTaskWebTest() {
        LocalDate limit_date = LocalDate.now().plusDays(1);
        Task task = new Task("Test title","Test description",LocalDate.now(),limit_date);
        CommonResponse<Task> response = new CommonResponse<Task>("Task created successfully", task);

        webClient.post()
                .uri("/tasks/")
                .bodyValue(task)
                .header("Authorization", "Bearer " + token.getObject())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .toString().equals(response.toString());
                
    }
    
    @Test
    public void findAllTasksWebTest() {

        webClient.get()
                .uri("/tasks/")
                .header("Authorization", "Bearer " + token.getObject())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("Test title")
                .jsonPath("$[0].user.email").isEqualTo("test@email.com");
    }
}
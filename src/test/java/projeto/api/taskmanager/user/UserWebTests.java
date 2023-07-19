package projeto.api.taskmanager.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.user.dtos.LoginDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UserWebTests {
    
    @Autowired
    private WebTestClient webClient;

    @BeforeAll
    private void setup() {
        User user = new User("test name", "test.user@email.com", "123");

        webClient.post()
                .uri("/users/")
                .bodyValue(user)
                .exchange();
    }
    

    @Test
    public void createUserWebTest() {
        User user = new User("test name", "testweb@email.com", "123");
        CommonResponse<User> response = new CommonResponse<User>("User created successfully", user);

        webClient.post()
                .uri("/users/")
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .toString().equals(response.toString());

    }

    @Test
    public void loginUserWebTest() {
        LoginDTO loginDTO = new LoginDTO("test.user@email.com", "123");
        webClient.post()
                .uri("/users/login")
                .bodyValue(loginDTO)
                .exchange()
                .expectStatus().isOk();
    }
}

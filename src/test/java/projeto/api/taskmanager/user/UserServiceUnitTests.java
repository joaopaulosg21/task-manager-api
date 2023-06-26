package projeto.api.taskmanager.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import projeto.api.taskmanager.exception.user.EmailAlreadyUsedException;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    private void setup() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void createUserTest() {
        User user = new User("test","test@email.com","123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO response = userService.create(user);

        assertEquals(user.getEmail(),response.email());
    }

    @Test
    public void createUserExceptionTest() {
        User user = new User("test","test@email.com","123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        EmailAlreadyUsedException response = assertThrows(EmailAlreadyUsedException.class, () -> userService.create(user));

        assertEquals("Email ja esta em uso",response.getMessage());
    }
}

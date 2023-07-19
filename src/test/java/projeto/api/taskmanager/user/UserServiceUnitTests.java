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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.configuration.authentication.TokenService;
import projeto.api.taskmanager.exception.user.EmailAlreadyUsedException;
import projeto.api.taskmanager.user.dtos.UserDTO;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    private UserService userService;

    @BeforeEach
    private void setup() {
        userService = new UserService(userRepository, passwordEncoder,authenticationManager,tokenService);
    }

    @Test
    public void createUserTest() {
        User user = new User(1L,"test","test@email.com","123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        CommonResponse<UserDTO> response = userService.create(user);

        assertEquals(user.getEmail(),response.getObject().getEmail());
        assertEquals("User created successfully",response.getMessage());
    }

    @Test
    public void createUserExceptionTest() {
        User user = new User("test","test@email.com","123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        EmailAlreadyUsedException response = assertThrows(EmailAlreadyUsedException.class, () -> userService.create(user));

        assertEquals("Email ja esta em uso",response.getMessage());
    }
}

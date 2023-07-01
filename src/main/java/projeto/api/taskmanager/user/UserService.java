package projeto.api.taskmanager.user;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import projeto.api.taskmanager.configuration.authentication.TokenService;
import projeto.api.taskmanager.exception.user.EmailAlreadyUsedException;
import projeto.api.taskmanager.user.dtos.LoginDTO;
import projeto.api.taskmanager.user.dtos.UserDTO;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public UserDTO create(User user) {
        Optional<User> optionalUser = repository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repository.save(user);

        return new UserDTO(savedUser.getId(),savedUser.getName(), savedUser.getEmail());
    }

    public Object login(LoginDTO loginDTO) {
        
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, loginDTO,null);

        Authentication authentication = authenticationManager.authenticate(auth);

        return tokenService.generate(authentication);
    }
}

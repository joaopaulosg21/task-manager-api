package projeto.api.taskmanager.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import projeto.api.taskmanager.exception.user.EmailAlreadyUsedException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserDTO create(User user) {
        Optional<User> optionalUser = repository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()) {
            throw new EmailAlreadyUsedException("Email ja está em uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repository.save(user);

        return new UserDTO(savedUser.getName(), savedUser.getEmail());
    }
}

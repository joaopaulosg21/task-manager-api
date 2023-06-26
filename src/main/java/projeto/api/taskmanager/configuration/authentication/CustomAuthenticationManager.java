package projeto.api.taskmanager.configuration.authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import projeto.api.taskmanager.exception.user.LoginException;
import projeto.api.taskmanager.user.User;
import projeto.api.taskmanager.user.UserRepository;
import projeto.api.taskmanager.user.dtos.LoginDTO;

public class CustomAuthenticationManager implements AuthenticationManager{

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginDTO loginDTO = (LoginDTO) authentication.getCredentials();

        Optional<User> optionalUser = repository.findByEmail(loginDTO.email());

        if(optionalUser.isEmpty()) {
            throw new LoginException();
        }

        if(!passwordEncoder.matches(loginDTO.password(), optionalUser.get().getPassword())) {
            throw new LoginException();
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(optionalUser.get(), loginDTO, null);

        return auth;
    }
    
}

package projeto.api.taskmanager.configuration.authentication;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import projeto.api.taskmanager.user.User;
import projeto.api.taskmanager.user.UserRepository;
import projeto.api.taskmanager.user.dtos.UserDTO;

@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter{
    
    private final TokenService tokenService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        String token = this.getTokenFromRequest(request);

        if(tokenService.isValid(token)) {
            this.authenticate(token);
        }

        doFilter(request, response, filterChain);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(token == null || token.isEmpty()) {
            return null;
        }

        return token.split(" ")[1];
    }

    private void authenticate(String token) {
        long id = tokenService.getIdFromToken(token);
        User user = userRepository.findById(id).get();
        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail());

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDTO, null, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
}

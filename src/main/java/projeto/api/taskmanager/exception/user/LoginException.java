package projeto.api.taskmanager.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LoginException extends RuntimeException{
    
    public LoginException() {
        super("Email ou password incorretos",null,false,false);
    }

}

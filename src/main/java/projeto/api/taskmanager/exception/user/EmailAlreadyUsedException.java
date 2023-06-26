package projeto.api.taskmanager.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyUsedException extends RuntimeException{
    
    public EmailAlreadyUsedException() {
        super("Email ja esta em uso",null,false,false);
    }

}

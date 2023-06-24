package projeto.api.taskmanager.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyUsedException extends RuntimeException{
    
    public EmailAlreadyUsedException(String msg) {
        super(msg,null,false,false);
    }

}

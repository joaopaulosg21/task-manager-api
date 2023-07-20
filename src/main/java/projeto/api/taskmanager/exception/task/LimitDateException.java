package projeto.api.taskmanager.exception.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LimitDateException extends RuntimeException{
    
    public LimitDateException() {
        super("Data limite não pode ser menor que a data de criação");
    }
}

package projeto.api.taskmanager.exception.task;

public class LimitDateException extends RuntimeException{
    
    public LimitDateException() {
        super("Data limite não pode ser menor que a data de criação");
    }
}

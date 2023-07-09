package projeto.api.taskmanager.common;

public record CommonResponse<T>(String message, T object) {
    
}

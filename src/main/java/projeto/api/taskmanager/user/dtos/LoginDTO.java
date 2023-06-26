package projeto.api.taskmanager.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank(message = "email não pode ser NULL")String email,
@NotBlank(message = "password não pode ser NULL")String password) {
    
}

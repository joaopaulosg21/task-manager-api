package projeto.api.taskmanager.user.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginDTO {
    
    @NotBlank(message = "email não pode ser NULL")
    private String email;

    @NotBlank(message = "password não pode ser NULL")
    private String  password;
}

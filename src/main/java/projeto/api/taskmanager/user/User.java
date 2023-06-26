package projeto.api.taskmanager.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name não pode ser NULL")
    private String name;

    @NotBlank(message = "email não pode ser NULL")
    private String email;

    @NotBlank(message = "password não pode ser NULL")
    private String password;

    public User(String name,String email,String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

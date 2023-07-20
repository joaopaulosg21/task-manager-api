package projeto.api.taskmanager.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.taskmanager.user.User;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    private long id;

    private String name;

    private String email;

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}

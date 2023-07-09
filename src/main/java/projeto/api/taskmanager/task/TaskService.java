package projeto.api.taskmanager.task;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.user.User;
import projeto.api.taskmanager.user.UserRepository;
import projeto.api.taskmanager.user.dtos.UserDTO;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public CommonResponse<Task> create(Task task, UserDTO userDTO) {
        User user = userRepository.findById(userDTO.id()).get();
        task.setUser(user);
        Task saved = taskRepository.save(task);

        return new CommonResponse<>("Task created successfully",saved);
    }
}

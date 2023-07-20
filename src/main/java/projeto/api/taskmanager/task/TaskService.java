package projeto.api.taskmanager.task;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.exception.task.LimitDateException;
import projeto.api.taskmanager.exception.task.TaskNotFoundException;
import projeto.api.taskmanager.user.User;
import projeto.api.taskmanager.user.UserRepository;
import projeto.api.taskmanager.user.dtos.UserDTO;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public CommonResponse<Task> create(Task task, UserDTO userDTO) {
        
        if(task.getLimit_date().compareTo(task.getCreatedAt()) < 0) {
            throw new LimitDateException();
        }

        User user = userRepository.findById(userDTO.getId()).get();
        task.setUser(user);
        task.setStatus(Status.CRIADA);
        Task saved = taskRepository.save(task);

        return new CommonResponse<>("Task created successfully",saved);
    }

    public List<Task> findAll(UserDTO userDTO) {
        return taskRepository.findAllByUserId(userDTO.getId());
    }

    public CommonResponse<Task> start(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(taskId, userDTO.getId()).orElseThrow(TaskNotFoundException::new);

        task.setStatus(Status.INICIADA);

        taskRepository.save(task);

        return new CommonResponse<Task>("task started successfully", task);

    }
}

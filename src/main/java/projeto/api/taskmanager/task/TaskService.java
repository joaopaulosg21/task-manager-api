package projeto.api.taskmanager.task;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        task.setCreatedAt(LocalDate.now());
        if(task.getLimit_date().compareTo(task.getCreatedAt()) < 0) {
            throw new LimitDateException();
        }

        User user = userRepository.findById(userDTO.getId()).get();
        task.setUser(user);
        task.setStatus(Status.CRIADA);
        Task saved = taskRepository.save(task);

        return new CommonResponse<>("Task created successfully",saved);
    }

    public Page<Task> findAll(UserDTO userDTO, Pageable pageable) {
        return taskRepository.findAllByUserId(userDTO.getId(),pageable);
    }

    public CommonResponse<Task> start(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(taskId, userDTO.getId()).orElseThrow(TaskNotFoundException::new);

        task.setStatus(Status.INICIADA);

        Task saved = taskRepository.save(task);

        return new CommonResponse<Task>("Task started successfully", saved);

    }
    
    public CommonResponse<Task> finish(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(taskId, userDTO.getId()).orElseThrow(TaskNotFoundException::new);
        task.setStatus(Status.TERMINADA);
        
        Task saved = taskRepository.save(task);


        return new CommonResponse<Task>("Task finished successfully", saved);
    }

    public Task findById(Long id, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(id, userDTO.getId()).orElseThrow(TaskNotFoundException::new);

        return task;
    }
}

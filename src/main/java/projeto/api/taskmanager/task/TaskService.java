package projeto.api.taskmanager.task;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.Cacheable;
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

    private final CacheManager cacheManager;

    public CommonResponse<Task> create(Task task, UserDTO userDTO) {
        task.setCreatedAt(LocalDate.now());
        if(task.getLimit_date().compareTo(task.getCreatedAt()) < 0) {
            throw new LimitDateException();
        }

        User user = userRepository.findById(userDTO.getId()).get();
        task.setUser(user);
        task.setStatus(Status.CRIADA);
        Task saved = taskRepository.save(task);
        
        for(int i=0; i < 10;i++) {
            Optional<ValueWrapper> optionalCache = Optional.ofNullable(cacheManager.getCache("tasks").get("AllTasks_"+userDTO.getId()+"_page="+i+"_size=10"));

            if(optionalCache.isEmpty()) {
                cacheManager.getCache("tasks").evictIfPresent("AllTasks_"+userDTO.getId()+"_page="+(i-1)+"_size=10");
                break;
            }
        }

        return new CommonResponse<>("Task created successfully",saved);
    }
    
    @Cacheable(value = "tasks",keyGenerator = "customKeyGenerator")
    public Page<Task> findAll(UserDTO userDTO, Pageable pageable) {
        return taskRepository.findAllByUserId(userDTO.getId(),pageable);
    }

    public CommonResponse<Task> start(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(taskId, userDTO.getId()).orElseThrow(TaskNotFoundException::new);

        task.setStatus(Status.INICIADA);

        Task saved = taskRepository.save(task);

        cacheManager.getCache("tasks").clear();
        return new CommonResponse<Task>("Task started successfully", saved);

    }
    
    public CommonResponse<Task> finish(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(taskId, userDTO.getId()).orElseThrow(TaskNotFoundException::new);
        task.setStatus(Status.TERMINADA);
        
        Task saved = taskRepository.save(task);

        cacheManager.getCache("tasks").clear();
        return new CommonResponse<Task>("Task finished successfully", saved);
    }

    public Task findById(Long id, UserDTO userDTO) {
        Task task = taskRepository.findByIdAndUserId(id, userDTO.getId()).orElseThrow(TaskNotFoundException::new);

        return task;
    }

    public Page<Task> findByStatus(Status status, UserDTO userDTO,Pageable pageable) {
        return taskRepository.findAllByStatusAndUserId(status, userDTO.getId(),pageable);
    }
}

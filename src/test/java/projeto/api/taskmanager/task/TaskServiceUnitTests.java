package projeto.api.taskmanager.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.exception.task.LimitDateException;
import projeto.api.taskmanager.user.User;
import projeto.api.taskmanager.user.UserRepository;
import projeto.api.taskmanager.user.dtos.UserDTO;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUnitTests {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    private void setup() {
        this.taskService = new TaskService(taskRepository, userRepository);
    }

    @Test
    public void createTaskTest() {
        LocalDate limit_date = LocalDate.now().plusDays(1);
        LocalDate createdAt = LocalDate.now();
        Task task = new Task("test","test description",createdAt,limit_date);

        UserDTO userDTO = new UserDTO(1L, "test user", "test.user@email.com");
        
        User user = new User(1L,"test user","test.user@email.com","123");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        CommonResponse<Task> response = taskService.create(task, userDTO);

        assertEquals("Task created successfully", response.getMessage());
        assertEquals(task.getTitle(),response.getObject().getTitle());

    }

    @Test
    public void createTaskLimitDateException() {
        LocalDate limit_date = LocalDate.now().minusDays(1);
        LocalDate createdAt = LocalDate.now();
        Task task = new Task("test","test description",createdAt,limit_date);
        UserDTO userDTO = new UserDTO(1L, "test user", "test.user@email.com");

        LimitDateException exception = assertThrows(LimitDateException.class,() -> taskService.create(task,userDTO));

        assertEquals("Data limite não pode ser menor que a data de criação",exception.getMessage());
    }

    @Test
    public void findAllTasksTest() {
        UserDTO userDTO = new UserDTO(1L, "test user", "test.user@email.com");

        List<Task> allTasks = new ArrayList<>();
        Task task = new Task("test","test description",LocalDate.now(),LocalDate.now().minusDays(1));
        allTasks.add(task);

        Page<Task> page = new PageImpl<>(allTasks);
        Pageable pageable = PageRequest.of(0,3);

        when(taskRepository.findAllByUserId(anyLong(),any(Pageable.class))).thenReturn(page);

        List<Task> response = taskService.findAll(userDTO,pageable).getContent();

        assertEquals(task.getTitle(),response.get(0).getTitle());
        assertEquals(task.getDescription(),response.get(0).getDescription());
    }

    @Test
    public void startTaskTest() {
        User user = new User(1L,"test user","test.user@email.com","123");
        Task task = new Task(1L,"test","test description",LocalDate.now(),LocalDate.now().minusDays(1),user,Status.CRIADA);

        when(taskRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        CommonResponse<Task> response = taskService.start(task.getId(), UserDTO.toDTO(user));

        assertEquals(Status.INICIADA,response.getObject().getStatus());
    }
    
    @Test
    public void finishTaskTest() {
        User user = new User(1L,"test user","test.user@email.com","123");
        Task task = new Task(1L,"test","test description",LocalDate.now(),LocalDate.now().minusDays(1),user,Status.CRIADA);
         
        when(taskRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        CommonResponse<Task> response = taskService.finish(task.getId(), UserDTO.toDTO(user));
        
        assertEquals(Status.TERMINADA, response.getObject().getStatus());
    }

    @Test
    public void findByIdTest() {
        User user = new User(1L,"test user","test.user@email.com","123");
        Task task = new Task(1L,"test","test description",LocalDate.now(),LocalDate.now().minusDays(1),user,Status.CRIADA);
         
        when(taskRepository.findByIdAndUserId(anyLong(),anyLong())).thenReturn(Optional.of(task));
        
        Task response = taskService.findById(task.getId(), UserDTO.toDTO(user));

        assertEquals(task.getTitle(),response.getTitle());
    }
}

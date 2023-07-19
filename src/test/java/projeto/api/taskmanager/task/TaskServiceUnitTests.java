package projeto.api.taskmanager.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}

package projeto.api.taskmanager.task;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import projeto.api.taskmanager.common.CommonResponse;
import projeto.api.taskmanager.user.dtos.UserDTO;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<CommonResponse<Task>> create(@Valid @RequestBody Task task, @AuthenticationPrincipal UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(task, userDTO));
    }

    @GetMapping()
    public ResponseEntity<List<Task>> findAll(@AuthenticationPrincipal UserDTO userDTO, Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(userDTO,pageable).getContent());
    }

    @PatchMapping("/start/{taskId}")
    public ResponseEntity<CommonResponse<Task>> start(@AuthenticationPrincipal UserDTO userDTO, @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.start(taskId, userDTO));
    }

    @PatchMapping("/finish/{taskId}")
    public ResponseEntity<CommonResponse<Task>> finish(@AuthenticationPrincipal UserDTO userDTO, @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.finish(taskId, userDTO));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> findById(@AuthenticationPrincipal UserDTO userDTO, @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.findById(taskId, userDTO));
    }
}

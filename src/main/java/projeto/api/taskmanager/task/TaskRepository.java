package projeto.api.taskmanager.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
    List<Task> findAllByUserId(Long userId);
}

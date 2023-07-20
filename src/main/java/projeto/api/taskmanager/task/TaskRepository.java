package projeto.api.taskmanager.task;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task,Long>{
    Page<Task> findAllByUserId(Long userId,Pageable pageable);

    Optional<Task> findByIdAndUserId(Long id, Long userId);
}

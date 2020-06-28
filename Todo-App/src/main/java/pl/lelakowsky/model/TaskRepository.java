package pl.lelakowsky.model;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
@Qualifier(value="sqlTaskRepository")
public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(Integer id);
    Task save(Task entity);
Page<Task> findAll(Pageable page);
    List<Task> findByDone(@Param("state") boolean done);
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
    boolean existsById(Integer id);
}

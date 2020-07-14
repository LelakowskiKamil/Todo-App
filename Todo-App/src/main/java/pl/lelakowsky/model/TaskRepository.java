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
    Page<Task> findAll(Pageable page);
    Optional<Task> findById(Integer id);
    boolean existsById(Integer id);
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
    List<Task> findByDone(@Param("state") boolean done);
    Task save(Task entity);

    List<Task> findAllByGroupId(Integer groupId);
}

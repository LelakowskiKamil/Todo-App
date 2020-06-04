package pl.lelakowsky.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lelakowsky.model.Project;
import pl.lelakowsky.model.ProjectRepository;
import pl.lelakowsky.model.TaskGroup;
import pl.lelakowsky.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project,Integer> {

    @Override
    @Query("from Project p join fetch p.steps")
    List<Project> findAll();
}

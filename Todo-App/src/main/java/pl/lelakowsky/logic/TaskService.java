package pl.lelakowsky.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.lelakowsky.model.Task;
import pl.lelakowsky.model.TaskRepository;

import javax.persistence.SecondaryTable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository repository;


    public TaskService(@Qualifier("sqlTaskRepository") final TaskRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        logger.info("supply async");
        return CompletableFuture.supplyAsync(repository::findAll);}
}

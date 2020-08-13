package pl.lelakowsky.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.lelakowsky.model.event.TaskDone;
import pl.lelakowsky.model.event.TaskUndone;

@Service
public class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    @EventListener
    public void on(TaskDone event){
        logger.info("Got "+event);
    }

    @EventListener
    public void on(TaskUndone event){
        logger.info("Got "+event);
    }
}

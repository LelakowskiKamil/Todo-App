package pl.lelakowsky.model.event;

import pl.lelakowsky.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent  {
    TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}

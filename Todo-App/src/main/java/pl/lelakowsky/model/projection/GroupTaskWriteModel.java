package pl.lelakowsky.model.projection;

import pl.lelakowsky.model.Task;
import pl.lelakowsky.model.TaskGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {

    private String description;
    private LocalDateTime deadline;

    Task toTask(final TaskGroup group){
        return new Task(description,deadline, group);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}

package pl.lelakowsky.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lelakowsky.model.Task;
import pl.lelakowsky.model.TaskGroup;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("Should create null deadline for group when no task deadlines")
    void constructor_noDeadLines_createsNullDeadline() {
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));

        var result = new GroupReadModel(source);

        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}
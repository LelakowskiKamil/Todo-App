package pl.lelakowsky.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lelakowsky.TaskConfigurationProperties;
import pl.lelakowsky.model.TaskGroup;
import pl.lelakowsky.model.TaskGroupRepository;
import pl.lelakowsky.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when undone tasks")
    void toggleGroup_undoneTasks_throws_IllegalStateException() {
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);

        var toTest = new TaskGroupService(null,mockTaskRepository);
       var exception =  catchThrowable(() ->toTest.ToggleGroup(1));
       assertThat(exception)
               .isInstanceOf(IllegalStateException.class)
               .hasMessageContaining("undone tasks");
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }

    @Test
    @DisplayName("should throw when no group ")
    void toggleGroup_wrongId_throws_IllegalArgumentException() {
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);

        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        var toTest = new TaskGroupService(mockRepository,mockTaskRepository);
        var exception =  catchThrowable(() ->toTest.ToggleGroup(1));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("should toggle group ")
    void toggleGroup_worksAsExpected() {
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
var group = new TaskGroup();
var beforeToggle=group.isDone();
        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(group));

        var toTest = new TaskGroupService(mockRepository,mockTaskRepository);
        toTest.ToggleGroup(0);
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }
}
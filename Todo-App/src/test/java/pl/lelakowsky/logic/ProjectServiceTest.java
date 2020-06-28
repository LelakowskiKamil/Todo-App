package pl.lelakowsky.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lelakowsky.TaskConfigurationProperties;
import pl.lelakowsky.model.*;
import pl.lelakowsky.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and other undone group exists")
    void createGroup_noMultipleGroupsConfig_and_undoneGroupExists_throwsIllegalStateExceptions() {
        //given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        //when

        var toTest = new ProjectService(null,mockGroupRepository,null,mockConfig);

        //when
        var exception = catchThrowable(() ->toTest.createGroup(LocalDateTime.now(), 0));


        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only one undone group from project is allowed");
       // assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

        }

        private TaskGroupService dummyGroupService(final InMemoryGroupRepository inMemoryGroupRepository){
        return new TaskGroupService(inMemoryGroupRepository,null);
        }

    @Test
    @DisplayName("should throw IllegalStateException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentExceptions() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository,null,null,mockConfig);

        //when
        var exception = catchThrowable(() ->toTest.createGroup(LocalDateTime.now(), 0));


        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
        // assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

    }
    private TaskConfigurationProperties configurationReturning(final boolean result){
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and no groups and no projects")
    void createGroup_noMultipleGroupsConfig_and_udoneGroupExists_noProjects_throwsIllegalArgumentExceptions() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        TaskGroupRepository mockGroupRepository= groupRepositoryReturning(false);

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository,mockGroupRepository,null,mockConfig);

        //when
        var exception = catchThrowable(() ->toTest.createGroup(LocalDateTime.now(), 0));


        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
        // assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

    }

    private TaskGroupRepository groupRepositoryReturning( final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    @Test
    @DisplayName("should create a new group from project")
    void create_configurationOk_existing_Project_creates_And_Saves_Group(){
        var today = LocalDate.now().atStartOfDay();
        var mockRepository = mock(ProjectRepository.class);
        var project =  projectWith("bar", Set.of(-1,-2));
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));

        InMemoryGroupRepository inMemoryGroupRepo = new InMemoryGroupRepository();
        var serviceWithInMemoryRepo = dummyGroupService(inMemoryGroupRepo);
int countBeforeCall = inMemoryGroupRepo.count();
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository,inMemoryGroupRepo,serviceWithInMemoryRepo,mockConfig);

        GroupReadModel result = toTest.createGroup(today,1);

        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));

        assertThat(countBeforeCall+1).isEqualTo(inMemoryGroupRepo.count());


    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline){
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days->{
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository InMemoryGroupRepository(){
       return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository{
            private Map<Integer, TaskGroup> map = new HashMap<>();
            private int index = 0;

            public int count() {
            return map.values().size();
            }

            @Override
            public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

            @Override
            public TaskGroup save(TaskGroup entity) {
            if (entity.getId()==0) {
                try {
                   var field = TaskGroup.class.getDeclaredField("id");
                   field.setAccessible(true);
                   field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(),entity);
            return entity;
        }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group-> group.getProject()!=null && group.getProject().getId() == projectId);
        }
        }
    }


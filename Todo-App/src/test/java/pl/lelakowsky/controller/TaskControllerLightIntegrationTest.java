package pl.lelakowsky.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.lelakowsky.model.Task;
import pl.lelakowsky.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        String description = "foo";
        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string(containsString(description)));
    }

}

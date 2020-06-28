package pl.lelakowsky.controller;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.lelakowsky.model.Task;
import pl.lelakowsky.model.TaskRepository;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Qualifier("sqlTaskRepository")
    @Autowired
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                                              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}

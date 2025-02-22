package by.vitikova.session.controller;

import by.vitikova.session.config.MongoDbContainerInitializer;
import by.vitikova.session.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest extends MongoDbContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionService sessionService;

    @Test
    void findByIdShouldReturnSessionIfExistAndStatus200() throws Exception {
        var login = "user_1";

        sessionService.getSession(login);

        mockMvc.perform(get("/api/v1/sessions?login=" + login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(login));
    }

    @Test
    void findByIdShouldReturnSessionIfNoExistAndStatus200() throws Exception {
        var login = "user_2";

        mockMvc.perform(get("/api/v1/sessions?login=" + login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(login));
    }
}
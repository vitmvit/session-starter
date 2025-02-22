package by.vitikova.session.controller;

import by.vitikova.session.config.PostgresSqlContainerInitializer;
import by.vitikova.session.model.dto.ErrorDto;
import by.vitikova.session.service.UserService;
import by.vitikova.session.util.UserTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void findByIdShouldReturnExpectedUserDtoAndStatus200() throws Exception {
        var id = 1L;
        var login = "user_1";

        var expected = userService.findById(id);

        mockMvc.perform(get("/api/v1/users/" + id + "?login=" + login))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void findByIdShouldReturnErrorDtoAndStatus400() throws Exception {
        var id = 2L;
        var login = "user_2";

        mockMvc.perform(get("/api/v1/users/" + id + "?login=" + login))
                .andExpect(status().isBadRequest())
                .andExpect(MvcResult::getResolvedException).getClass().equals(ErrorDto.class);
    }

    @Test
    public void createShouldReturnExpectedUserDtoAndStatus200() throws Exception {
        var userOpenTimeDto = UserTestBuilder.builder().build().buildUserOpenTimeDto();

        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(userOpenTimeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userOpenTimeDto.login()))
                .andExpect(jsonPath("$.password").value(userOpenTimeDto.password()));
    }
}
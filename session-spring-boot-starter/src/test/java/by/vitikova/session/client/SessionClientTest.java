package by.vitikova.session.client;

import by.vitikova.session.config.SessionAutoConfiguration;
import by.vitikova.session.property.SessionManagementProperty;
import by.vitikova.session.util.SessionTestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

//@WireMockTest
//@SpringBootTest
//@Import(SessionAutoConfiguration.class)
@WireMockTest(httpPort = 9998)
public class SessionClientTest {

    private final RestTemplate restTemplate = new RestTemplate();
    @MockBean
    private final SessionManagementProperty sessionManagementProperty = new SessionManagementProperty();
    private final SessionClient sessionClient = new SessionClient(restTemplate, sessionManagementProperty);

//    @MockBean
//    private SessionManagementProperty sessionManagerProperties;
//
//    @Autowired
//    private SessionClient sessionClient;


//    @BeforeEach
//    void setUp() {
//        // Устанавливаем URL для тестирования
//        when(sessionManagementProperty.getSessionProviderUrl()).thenReturn("http://localhost:8080");
//    }

    @AfterEach
    void tearDown() {
        // Очищаем все моки после каждого теста
        WireMock.reset();
    }

    @Test
    public void getNewsByIdShouldReturnExpectedNewsDto() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        var sessionDto = SessionTestBuilder.builder().build().buildSessionDto();
        var sessionCreateDto = SessionTestBuilder.builder().build().buildSessionCreateDto();
        var login = sessionDto.login();

        stubFor(get(urlPathTemplate("/api/v1/sessions?login=" + login))
                .withPathParam("login", matching("^(.*)([0-9][a-z][A-Z]+)$"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(sessionDto))));

        var actual = sessionClient.getSession(sessionCreateDto);

        assertEquals(sessionDto.id(), actual.id());
        assertEquals(sessionDto.login(), actual.login());
        assertEquals(sessionDto.dateCreate(), actual.dateCreate());
    }
}
package by.vitikova.session.client;

import by.vitikova.session.service.UserTestService;
import by.vitikova.session.util.SessionTestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertSame;

@ActiveProfiles("test")
@WireMockTest(httpPort = 9998)
public class UserTestServiceTest {

    private final UserTestService testClass = new UserTestService();

    @Test
    public void createSessionShouldReturnExpectedSessionDto() throws JsonProcessingException {
        var userOpenTimeDto = SessionTestBuilder.builder().build().buildUserOpenTimeDto();
        var expectedSessionDto = SessionTestBuilder.builder().build().buildSessionDto();
        var login = expectedSessionDto.login();

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        stubFor(get(urlPathTemplate("/api/v1/sessions?login=" + login))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedSessionDto))));

        var actual = testClass.getSession(userOpenTimeDto, expectedSessionDto);

        assertSame(expectedSessionDto, actual);
    }
}
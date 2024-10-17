package by.vitikova.session.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "sessions")
@NoArgsConstructor
public class Session {

    @Id
    private String id;

    private String login;
    private LocalDateTime dateCreate;

    public Session(String login) {
        this.login = login;
    }
}
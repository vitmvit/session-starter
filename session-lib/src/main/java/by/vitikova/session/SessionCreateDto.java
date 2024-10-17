package by.vitikova.session;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class SessionCreateDto implements SessionInfo {
    private final String login;

    //    public SessionCreateDto(String login) {
//        this.login = login;
//    }
//
    @Override
    public String login() {
        return login;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) return true;
//        if (obj == null || obj.getClass() != this.getClass()) return false;
//        var that = (SessionCreateDto) obj;
//        return Objects.equals(this.login, that.login);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(login);
//    }
//
//    @Override
//    public String toString() {
//        return "SessionCreateDto[" +
//                "login=" + login + ']';
//    }
}
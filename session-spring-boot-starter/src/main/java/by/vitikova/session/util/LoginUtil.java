package by.vitikova.session.util;

import by.vitikova.session.SessionDto;
import by.vitikova.session.SessionInfo;
import by.vitikova.session.exception.AnnotationException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static by.vitikova.session.constant.Constant.BAD_LOGIN_MESSAGE;

/**
 * Утилитный класс для работы с логинами и сессиями.
 * <p>
 * Этот класс предназначен для предоставления статических методов, связанных
 * с извлечением логинов, проверкой черных списков и заменой аргументов сессии.
 */
@UtilityClass
public class LoginUtil {

    /**
     * Извлекает логин из аргументов метода, используя параметры.
     *
     * @param args       аргументы метода
     * @param parameters параметры метода
     * @return извлеченный логин из аргументов
     * @throws AnnotationException если логин не найден или пуст
     */
    public static String extractLoginFromArgs(Object[] args, Parameter[] parameters) {
        return IntStream.range(0, args.length)
                .mapToObj(i -> {
                    Parameter parameter = parameters[i];
                    if (SessionInfo.class.isAssignableFrom(parameter.getType())) {
                        SessionInfo login = (SessionInfo) args[i];
                        return Optional.ofNullable(login.login())
                                .filter(s -> !s.trim().isEmpty())
                                .orElseThrow(() -> new AnnotationException(BAD_LOGIN_MESSAGE));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new AnnotationException(BAD_LOGIN_MESSAGE));
    }

    /**
     * Проверяет, отсутствует ли логин в черном списке.
     *
     * @param login     логин для проверки
     * @param blackList набор значений черного списка
     * @return true, если логин не в черном списке; иначе false
     */
    public static boolean isLoginNotInBlackList(String login, Set<String> blackList) {
        return !blackList.contains(login);
    }

    /**
     * Проверяет, была ли сессия предоставлена в качестве параметров метода.
     *
     * @param args аргументы метода
     * @return true, если в аргументах присутствует объект типа {@link SessionDto}; иначе false
     */
    public static boolean isSessionProvidedInParams(Object[] args) {
        return Arrays.stream(args).anyMatch(SessionDto.class::isInstance);
    }

    /**
     * Заменяет аргументы метода объектом сессии.
     *
     * @param args    аргументы метода
     * @param session объект сессии, который заменит {@link SessionDto}
     * @return новый массив аргументов, с замененной сессией
     */
    public static Object[] substituteArgs(Object[] args, SessionDto session) {
        return Arrays.stream(args)
                .map(arg -> arg instanceof SessionDto ? session : arg).toArray();
    }
}
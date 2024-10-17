package by.vitikova.session.exception;

/**
 * Исключение, возникающее при ошибках, связанных с сервисами сессий.
 * <p>
 * Это исключение является подклассом {@link RuntimeException} и
 * предназначено для обработки случаев, когда операции над сессией
 * завершаются с ошибкой.
 */
public class SessionServiceException extends RuntimeException {

    public SessionServiceException(String message) {
        super(message);
    }
}
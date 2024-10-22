package by.vitikova.session.exception;

/**
 * Исключение, возникающее при ошибках, связанных с аннотациями.
 * <p>
 * Это исключение является подклассом {@link RuntimeException} и
 * предназначено для обработки случаев, когда аннотации не могут быть
 * корректно обработаны.
 */
public class AnnotationException extends RuntimeException {

    public AnnotationException(String message) {
        super(message);
    }
}
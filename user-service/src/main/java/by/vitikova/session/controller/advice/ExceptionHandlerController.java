package by.vitikova.session.controller.advice;

import by.vitikova.session.exception.AnnotationException;
import by.vitikova.session.exception.OperationException;
import by.vitikova.session.exception.SessionServiceException;
import by.vitikova.session.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Контроллер обработки исключений.
 * <p>
 * Этот класс предназначен для перехвата и обработки исключений,
 * возникающих в приложении, и формирования соответствующих
 * ответов с информацией об ошибках.
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Обработчик для {@link AnnotationException}.
     *
     * @param e исключение, возникшее в результате ошибок обработки аннотаций
     * @return объект {@link ErrorDto} с сообщением об ошибке и кодом состояния 400 (BAD_REQUEST)
     */
    @ExceptionHandler(AnnotationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto error(AnnotationException e) {
        return new ErrorDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Обработчик для {@link SessionServiceException}.
     *
     * @param e исключение, возникшее в результате ошибок в сервисах сессий
     * @return объект {@link ErrorDto} с сообщением об ошибке и кодом состояния 403 (FORBIDDEN)
     */
    @ExceptionHandler(SessionServiceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto error(SessionServiceException e) {
        return new ErrorDto(e.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    /**
     * Обработчик для {@link OperationException}.
     *
     * @param e исключение, возникшее при выполнении операций
     * @return объект {@link ErrorDto} с сообщением об ошибке и кодом состояния 500 (INTERNAL_SERVER_ERROR)
     */
    @ExceptionHandler(OperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto error(OperationException e) {
        return new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Общий обработчик исключений для всех остальных случаев.
     * <p>
     * Этот метод ловит все остальные исключения и возвращает общий
     * ответ об ошибке.
     *
     * @param e общее исключение {@link Exception}, которое может произойти в приложении.
     * @return объект {@link ErrorDto} с сообщением об ошибке и статусом 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto error(Exception e) {
        return new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
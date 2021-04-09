package br.com.JoaoS.pockafkamongodb.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class CarExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarException.class)
    protected ResponseEntity<Object> handleConflict(CarException ex, WebRequest request) {
        return this.handleExceptionInternal(ex, null, new HttpHeaders(), ex.httpStatus, request);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {
        CarExceptionDto carException = new CarExceptionDto(
                new Date(),
                ex.getMessage(),
                Objects.requireNonNullElse(status, HttpStatus.INTERNAL_SERVER_ERROR).value(),
                Objects.requireNonNullElse(status, HttpStatus.INTERNAL_SERVER_ERROR).getReasonPhrase(),
                request.getDescription(false)
        );
        return  new ResponseEntity(carException,
                headers,
                Objects.requireNonNullElse(status, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}

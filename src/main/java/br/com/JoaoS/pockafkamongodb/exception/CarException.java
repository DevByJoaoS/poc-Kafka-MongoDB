package br.com.JoaoS.pockafkamongodb.exception;

import org.springframework.http.HttpStatus;

public class CarException extends RuntimeException{

    public String message;

    public HttpStatus httpStatus;

    public CarException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public CarException(String message, HttpStatus httpStatus, Throwable e) {
        super(message, e, false, false);
    }

}

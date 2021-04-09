package br.com.JoaoS.pockafkamongodb.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
public class CarExceptionDto {
    private Date timestamp;
    private String message;
    private Integer status;
    private String error;
    private String path;
}

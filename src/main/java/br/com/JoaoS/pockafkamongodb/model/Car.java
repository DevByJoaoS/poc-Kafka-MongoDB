package br.com.JoaoS.pockafkamongodb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Car")
public class Car {

    @Id
    private String id;

    private String model;

    private String brand;

    private BigDecimal price;

    private Long quantity;
}

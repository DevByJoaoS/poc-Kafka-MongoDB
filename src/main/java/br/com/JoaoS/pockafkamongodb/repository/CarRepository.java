package br.com.JoaoS.pockafkamongodb.repository;

import br.com.JoaoS.pockafkamongodb.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CarRepository extends MongoRepository<Car, String> {

    Optional<Car> findByModel(String model);
}

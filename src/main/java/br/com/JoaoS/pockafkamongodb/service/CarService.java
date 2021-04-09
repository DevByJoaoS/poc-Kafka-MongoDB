package br.com.JoaoS.pockafkamongodb.service;

import br.com.JoaoS.pockafkamongodb.exception.CarException;
import br.com.JoaoS.pockafkamongodb.model.Car;
import br.com.JoaoS.pockafkamongodb.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CarService {

    private static final Logger log = LoggerFactory.getLogger(CarService.class);

    private final KafkaTemplate<String, Car> kafkaTemplate;

    private final String CAR_TOPIC = "car-order-topic";

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository, KafkaTemplate<String, Car> kafkaTemplate){
        this.carRepository = carRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Car create(Car car){
        car.setId(UUID.randomUUID().toString());
        return this.carRepository.save(car);
    }

    public Car updateCar(Car car){
        Car existingCar = this.carRepository.findById(car.getId()).orElse(null);

        if(Objects.isNull(existingCar)){
            throw new CarException("No car found to update", HttpStatus.BAD_REQUEST);
        }

        return this.carRepository.save(car);
    }

    public List<Car> findAll(){
        return this.carRepository.findAll();
    }

    public Car findById(String id) {
        Car car =  this.carRepository.findById(id).orElse(null);

        if(Objects.isNull(car)){
            throw new CarException("No car found with this id", HttpStatus.BAD_REQUEST);
        }

        return car;
    }

    public Car findByModel(String model) {
        Car car = this.carRepository.findByModel(model).orElse(null);

        if(Objects.isNull(car)){
            throw new CarException("We don't have this model in stock", HttpStatus.BAD_REQUEST);
        }

        return car;
    }

    public void deleteById(String id){
        Car car =  this.carRepository.findById(id).orElse(null);

        if(Objects.isNull(car)){
            throw new CarException("No car found to delete", HttpStatus.BAD_REQUEST);
        }

        this.carRepository.deleteById(id);
    }

    public void sendCarOrder(Car car) {
        if(Objects.isNull(car.getId()) && Objects.isNull(car.getModel())){
            throw new CarException("You must fill either the id or the model to order a car", HttpStatus.BAD_REQUEST);
        }

        kafkaTemplate.send(CAR_TOPIC, car);
    }

    public void carOrder(Car car) {
        Car existingCar = this.carRepository.findById(car.getId()).orElse(null);
        if(Objects.isNull(existingCar)) {
            existingCar = this.carRepository.findByModel(car.getModel()).orElse(null);
        }

        if(Objects.isNull(existingCar)){
            log.info("There is no {} in stock", car.getModel());
        }else {
            existingCar.setQuantity(existingCar.getQuantity() - 1);

            if (existingCar.getQuantity() == 0) {
                log.info("Car model {} out of stock, last order accepted", car.getModel());
                deleteById(existingCar.getId());
            } else {
                log.info("Car order completed");
                updateCar(existingCar);
            }
        }
    }

}

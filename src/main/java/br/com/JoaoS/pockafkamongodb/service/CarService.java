package br.com.JoaoS.pockafkamongodb.service;

import br.com.JoaoS.pockafkamongodb.model.Car;
import br.com.JoaoS.pockafkamongodb.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        return this.carRepository.save(car);
    }

    public List<Car> findAll(){
        return this.carRepository.findAll();
    }

    public Car findById(String id) {
        return this.carRepository.findById(id).orElse(null);
    }

    public Car findByModel(String model) {
        return this.carRepository.findByModel(model).orElse(null);
    }

    public void deleteById(String id){
        this.carRepository.deleteById(id);
    }

    public void sendCarOrder(Car car) {
        kafkaTemplate.send(CAR_TOPIC, car);
    }

    public void carOrder(Car car) {
        Car existingCar = findById(car.getId());
        if(Objects.isNull(existingCar)) {
            existingCar = findByModel(car.getModel());
        }

        if(Objects.isNull(existingCar)){
            log.info("There is no {} in the stock", car.getModel());
        } else {

            existingCar.setQuantity(existingCar.getQuantity()-1);

            if(existingCar.getQuantity() == 0){
                log.info("Car model {} out of stock, last order accepted", car.getModel());
                deleteById(existingCar.getId());
            }else {
                log.info("Car order completed");
                updateCar(existingCar);
            }

        }
    }

}

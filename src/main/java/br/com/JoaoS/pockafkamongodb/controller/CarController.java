package br.com.JoaoS.pockafkamongodb.controller;

import br.com.JoaoS.pockafkamongodb.model.Car;
import br.com.JoaoS.pockafkamongodb.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public Car saveCar(@RequestBody Car car) {
        return this.carService.create(car);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return this.carService.findAll();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable("id") String id) {
        return this.carService.findById(id);
    }

    @GetMapping("/model/{model}")
    public Car getCarByModel(@PathVariable("model") String model) {
        return this.carService.findByModel(model);
    }

    @DeleteMapping("/{id}")
    public void deleteCarById(@PathVariable("id") String id){
        this.carService.deleteById(id);
    }

    @PostMapping("/order")
    public void sendOrder(@RequestBody Car car) {
        this.carService.sendCarOrder(car);
    }



}

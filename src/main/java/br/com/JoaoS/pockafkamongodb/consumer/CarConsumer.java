package br.com.JoaoS.pockafkamongodb.consumer;

import br.com.JoaoS.pockafkamongodb.model.Car;
import br.com.JoaoS.pockafkamongodb.service.CarService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CarConsumer {

    @Value("${spring.kafka.consumer.group-id}")
    private final String groupId = "";

    private CarService carService;

    @Autowired
    public CarConsumer( CarService carService){
        this.carService = carService;
    }

    @KafkaListener( topics = "car-order-topic", id = groupId, containerFactory = "carKafkaListenerFactory")
    public void carListener(ConsumerRecord<String, Car> car) {
        this.carService.carOrder(car.value());
    }

}

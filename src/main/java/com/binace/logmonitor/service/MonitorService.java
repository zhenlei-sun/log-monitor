package com.binace.logmonitor.service;

import io.prometheus.client.Counter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class MonitorService {


    public static final Counter REGISTER_USER_COUNTER = Counter.build()
            .name("register_user_number")
            .labelNames("users")
            .help("total registered user numbers")
            .register();
    /**
     * 监听器自动执行该方法 消费消息
     */
    @KafkaListener(topics = "${spring.kafka.topic}")
    public void processMsg(ConsumerRecord record) {
        Optional optional = Optional.ofNullable(record);
        if(optional.isPresent()){
            String message = record.value().toString();
            if(message.contains("register_user_number")){
                REGISTER_USER_COUNTER.labels("users").inc();
            }
            else if(message.isEmpty()){

            }
            else{

            }
        }
    }
}

package com.binace.logmonitor.service;

import com.binace.logmonitor.utils.Constants;
import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Slf4j
public class MonitorService {

    /**
     * 监听器 消费消息
     */
    @KafkaListener(topics = "${spring.kafka.topic}")
    public void processMsg(ConsumerRecord record) {
        Optional optional = Optional.ofNullable(record);
        if(optional.isPresent()){
            String message = record.value().toString();
            if(message.contains("register_user_number")){
                Constants.REGISTER_USER_COUNTER.labels("users").inc();
            }
            else if(message.contains("total_register_number")){
                String msg = record.value().toString();
                String regEx = ".*total_register_number.*(\\d+).*";
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher(msg);
                if(mat.find()){
                    Constants.REGISTER_USER_COUNTER.labels("users").set(Double.valueOf(mat.group(1)));
                }
            }
            else{

            }
        }
        else {
            log.warn("record is null.");
        }
    }
}

package com.binace.logmonitor.service;

import com.binace.logmonitor.utils.Constants;
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
        if(optional.isPresent()) {
            String message = record.value().toString();
            if(message.contains("register:注册结束")){
                Constants.ACCOUNT_USER_COUNTER.labels(Constants.ACCOUNT_USER_COUNTER_LABEL_REGISTER).inc();
            }
            else if(message.contains("注册总人数")){
                String msg = record.value().toString();
                String regEx = ".*注册总人数.*(\\d+).*";
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher(msg);
                if(mat.find()){
                    Constants.ACCOUNT_USER_COUNTER.labels(Constants.ACCOUNT_USER_COUNTER_LABEL_REGISTER).set(Double.valueOf(mat.group(1)));
                }
            }
            else if (message.contains("用户登录成功")){
                //登陆人数
                Constants.ACCOUNT_USER_COUNTER.labels(Constants.ACCOUNT_USER_COUNTER_LABEL_LOGIN).inc();
                //登陆人数-用于计算在线人数
                Constants.ACCOUNT_ONLINE_USER_GAUGE.inc();
            }
            else if (message.contains("用户下线")){
                Constants.ACCOUNT_ONLINE_USER_GAUGE.dec();
            }
        }
        else {
            log.warn("record is null.");
        }
    }
}

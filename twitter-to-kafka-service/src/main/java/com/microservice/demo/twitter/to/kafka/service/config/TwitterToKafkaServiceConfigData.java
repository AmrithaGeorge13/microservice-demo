package com.microservice.demo.twitter.to.kafka.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix="twitter-to-kafka-service")
public class TwitterToKafkaServiceConfigData {
    private List<String> twitterKeyWords;
    private String welcomeMessage;


    public List<String> getTwitterKeyWords(){
        return twitterKeyWords;
    }

    public String getWelcomeMessage(){
        return welcomeMessage;
    }
}

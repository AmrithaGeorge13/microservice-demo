package com.microservice.demo.twitter.to.kafka.service;

import com.microservice.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;


@SpringBootApplication
public class TwitterToKafkaSerice implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterToKafkaSerice.class);
    TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    TwitterToKafkaSerice(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
    }
    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaSerice.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Twitter To Kafka Service started: "+twitterToKafkaServiceConfigData.getWelcomeMessage());
        LOGGER.info(Arrays.toString(twitterToKafkaServiceConfigData.getTwitterKeyWords().toArray(new String[]{})));

    }
}
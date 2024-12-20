package com.microservice.demo.twitter.to.kafka.service;

import com.microservice.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import com.microservice.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;


@SpringBootApplication
public class TwitterToKafkaSerice implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterToKafkaSerice.class);
    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final StreamRunner streamRunner;

    TwitterToKafkaSerice(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData, StreamRunner streamRunner) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.streamRunner = streamRunner;
    }
    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaSerice.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Twitter To Kafka Service started: "+twitterToKafkaServiceConfigData.getWelcomeMessage());
        LOGGER.info(Arrays.toString(twitterToKafkaServiceConfigData.getTwitterKeyWords().toArray(new String[]{})));
        streamRunner.start();
    }
}
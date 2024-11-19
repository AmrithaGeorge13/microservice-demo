package com.microservice.demo.twitter.to.kafka.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TwitterToKafkaSerice implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaSerice.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Twitter To Kafka Service");
    }
}
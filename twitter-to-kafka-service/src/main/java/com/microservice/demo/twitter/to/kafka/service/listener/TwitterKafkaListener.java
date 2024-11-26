package com.microservice.demo.twitter.to.kafka.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterKafkaListener extends StatusAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaListener.class);

    @Override
    public void onStatus(Status status) {
        LOG.info("Twitter status with text " + status.getText());
    }

    @Override
    public void onException(Exception ex) {
        LOG.error("Exception Occured {}", ex.getMessage());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        LOG.info("Track limit reached: " + numberOfLimitedStatuses);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        LOG.info("Stall warning: " + warning.getMessage());
    }
}

package com.microservice.demo.twitter.to.kafka.service.runner.impl;

import com.microservice.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import com.microservice.demo.twitter.to.kafka.service.listener.TwitterKafkaListener;
import com.microservice.demo.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Arrays;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwiterKafkaStreamRunnerImpl implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwiterKafkaStreamRunnerImpl.class);
    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaListener twitterKafkaListener;
    private TwitterStream twitterStream;

    public TwiterKafkaStreamRunnerImpl(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData, TwitterKafkaListener twitterKafkaListener) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.twitterKafkaListener = twitterKafkaListener;
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaListener);
        addFilter();
    }

    @PreDestroy
    public void shutdown() throws TwitterException {
        if (twitterStream != null) {
            LOG.info("Shutting down twitter stream");
            twitterStream.shutdown();
        }
    }

    /**
     * This method filters data from twitter stream for keywords defined in config
     **/
    private void addFilter() {
        String[] keyWords = twitterToKafkaServiceConfigData.getTwitterKeyWords().toArray(new String[0]);
        FilterQuery query = new FilterQuery(keyWords);
        twitterStream.filter(query);
        LOG.info("Twitter  started streaming for keywords {}", Arrays.toString(keyWords));
    }
}

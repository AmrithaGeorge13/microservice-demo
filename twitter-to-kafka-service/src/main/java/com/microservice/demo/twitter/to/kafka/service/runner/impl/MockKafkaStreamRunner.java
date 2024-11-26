package com.microservice.demo.twitter.to.kafka.service.runner.impl;

import com.microservice.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import com.microservice.demo.twitter.to.kafka.service.exception.TwitterToKafkaException;
import com.microservice.demo.twitter.to.kafka.service.listener.TwitterKafkaListener;
import com.microservice.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunner.class);
    private static final Random RANDOM = new Random();
    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static final String[] WORDS = new String[]{"Lorem", "ipsum", "dolor", "sit", "amet", "consectetuer", "adipiscing", "elit", "Maecenas", "porttitor", "congue", "massa", "Fusce", "posuere", "magna", "sed", "pulvinar", "ultricies", "purus", "lectus", "malesuada", "libero"};
    private static final String tweetAsRawJson = "{" + "\"created_at\":\"{0}\"," + "\"id\":\"{1}\"," + "\"text\":\"{2}\"," + "\"user\":{\"id\":\"{3}\"}" + "}";
    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaListener twitterKafkaListener;

    public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData, TwitterKafkaListener twitterKafkaListener) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.twitterKafkaListener = twitterKafkaListener;
    }

    private static void constructRandomTweets(String[] keyWords, int tweetLength, StringBuilder tweet) {
        for (int i = 0; i < tweetLength; i++) {
            tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if (i == tweetLength / 2) {
                tweet.append(keyWords[RANDOM.nextInt(keyWords.length)]).append(" ");
            }
        }
    }

    private String getFormattedTweet(String[] keyWords, int minTweetLength, int maxTweetLength) {
        String[] parms = new String[]{ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)), String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)), getRandomTweetContent(keyWords, minTweetLength, maxTweetLength), String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))};
        String tweet = tweetAsRawJson;
        for (int i = 0; i < parms.length; i++) {
            tweet = tweet.replace("{" + i + "}", parms[i]);
        }
        return tweet;
    }

    private String getRandomTweetContent(String[] keyWords, int minTweetLength, int maxTweetLength) {
        StringBuilder tweet = new StringBuilder();
        int tweetLength = RANDOM.nextInt(maxTweetLength - minTweetLength + 1) + minTweetLength;
        constructRandomTweets(keyWords, tweetLength, tweet);
        return tweet.toString().trim();
    }

    @Override
    public void start() {
        String[] keyWords = twitterToKafkaServiceConfigData.getTwitterKeyWords().toArray(new String[0]);
        int minTweetLength = twitterToKafkaServiceConfigData.getMinTweetLength();
        int maxTweetLength = twitterToKafkaServiceConfigData.getMaxTweetLength();
        long sleepTimeMs = twitterToKafkaServiceConfigData.getMockSleepMs();
        LOG.info("Starting Mock filtering twiter streams for keywords {}", Arrays.toString(keyWords));
        simulateTwitterStream(keyWords, minTweetLength, maxTweetLength, sleepTimeMs);

    }

    private void simulateTwitterStream(String[] keyWords, int minTweetLength, int maxTweetLength, long sleepTimeMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    String fomrattedTweetAsRawJson = getFormattedTweet(keyWords, minTweetLength, maxTweetLength);
                    Status status = TwitterObjectFactory.createStatus(fomrattedTweetAsRawJson);
                    twitterKafkaListener.onStatus(status);
                    sleep(sleepTimeMs);
                } catch (TwitterException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaException("Error while sleeping for waiting new status to create", e);
        }
    }
}

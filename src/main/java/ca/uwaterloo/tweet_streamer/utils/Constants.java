package ca.uwaterloo.tweet_streamer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Constants
{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String STREAM_HOST = "https://stream.twitter.com";
    public static final Integer TWITTER_QUEUE_SIZE = 10000;
}

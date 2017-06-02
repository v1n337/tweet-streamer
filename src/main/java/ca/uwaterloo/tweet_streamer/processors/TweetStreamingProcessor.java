package ca.uwaterloo.tweet_streamer.processors;

import ca.uwaterloo.tweet_streamer.beans.TwitterCredentials;
import ca.uwaterloo.tweet_streamer.utils.Constants;
import ca.uwaterloo.tweet_streamer.utils.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class TweetStreamingProcessor extends Processor
{
    private Logger log = LogManager.getLogger(getClass());

    @Override
    public void process()
        throws Exception
    {
        log.info("TweetStreamingProcessor begun");

        TwitterCredentials twitterCredentials =
            Constants.OBJECT_MAPPER.readValue(
                new File(Options.getInstance().getTwitterCredentialsFilepath()),
                TwitterCredentials.class
            );
        log.debug("twitterCredentials: " + twitterCredentials);

        log.info("TweetStreamingProcessor completed");
    }
}

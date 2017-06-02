package ca.uwaterloo.tweet_streamer.processors;

import ca.uwaterloo.tweet_streamer.beans.TwitterCredentials;
import ca.uwaterloo.tweet_streamer.utils.Constants;
import ca.uwaterloo.tweet_streamer.utils.Options;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Lists.newArrayList("twitterapi", "#yolo"));

        Authentication auth =
            new OAuth1(
                twitterCredentials.getConsumerKey(), twitterCredentials.getConsumerSecret(),
                twitterCredentials.getAccessTokenKey(), twitterCredentials.getAccessTokenSecret()
            );

        // Create a new BasicClient. By default gzip is enabled.
        Client client = new ClientBuilder()
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(auth)
            .processor(new StringDelimitedProcessor(queue))
            .build();

        // Establish a connection
        client.connect();

        // Do whatever needs to be done with messages
        for (int msgRead = 0; msgRead < 1000; msgRead++) {
            String msg = queue.take();
            System.out.println(msg);
        }

        client.stop();

        log.info("TweetStreamingProcessor completed");
    }
}

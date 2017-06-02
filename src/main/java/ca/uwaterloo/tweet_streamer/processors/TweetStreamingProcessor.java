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
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TweetStreamingProcessor extends Processor
{
    private Logger log = LogManager.getLogger(getClass());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(Constants.TWITTER_QUEUE_SIZE);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Lists.newArrayList("$"));
        endpoint.languages(Lists.newArrayList("en"));

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

        while (true)
        {
            String msg = queue.take();
            Status status = TwitterObjectFactory.createStatus(msg);

            if (status.getSymbolEntities().length > 0)
            {
                log.debug("Writing tweet " + status.getId() + " to file");
                writeToFile(
                    status.getText().replace("\t", ""),
                    status.getId(), status.getCreatedAt(), Options.getInstance().getOutputDirectory()
                );
            }
        }
    }

    private void writeToFile(String tweetText, Long tweetId, Date tweetTimestamp, String outputFolder)
        throws IOException
    {
        String outputFilepath =
            outputFolder + "/streamed_tweets_" + sdf.format(tweetTimestamp) + ".tsv";
        File outputFile = new File(outputFilepath);

        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(tweetId + "\t" + tweetTimestamp.getTime() + "\t" + tweetText + "\n");
        bw.close();
        fw.close();
    }
}

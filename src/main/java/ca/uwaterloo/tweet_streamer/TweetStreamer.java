package ca.uwaterloo.tweet_streamer;

import ca.uwaterloo.tweet_streamer.processors.Processor;
import ca.uwaterloo.tweet_streamer.processors.TweetStreamingProcessor;
import ca.uwaterloo.tweet_streamer.utils.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TweetStreamer
{

    public static void main(String[] args)
    {
        Logger log = LogManager.getLogger("main");

        try
        {
            log.info("Application bootstrapping");
            Options.initializeInstance(args);

            Processor processor = new TweetStreamingProcessor();
            processor.process();

            log.info("Application run complete");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}

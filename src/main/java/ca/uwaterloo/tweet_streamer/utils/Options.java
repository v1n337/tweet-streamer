package ca.uwaterloo.tweet_streamer.utils;

import ca.uwaterloo.tweet_streamer.exceptions.InternalAppError;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

@Getter
public class Options
{
    private static Options instance;

    private Logger log = LogManager.getLogger(getClass());

    @Option(name = "-help", usage = "Help", metaVar = "HELP")
    private Boolean help = false;

    @Option(name = "-twitter-creds-filepath", usage = "Twitter Creds", required = true)
    private String twitterCredentialsFilepath;

    public static void initializeInstance(String[] args)
        throws CmdLineException
    {
        if (null == instance)
        {
            instance = new Options(args);
        }
    }

    public static Options getInstance()
        throws InternalAppError
    {
        if (null == instance)
        {
            throw new InternalAppError("Tried accessing options without initializing it first.");
        }
        return instance;
    }

    private Options(String[] args)
        throws CmdLineException
    {
        CmdLineParser parser = new CmdLineParser(this);

        if (help)
        {
            parser.printUsage(System.out);
            System.exit(0);
        }
        parser.parseArgument(args);


        log.info("Options successfully read");
    }
}

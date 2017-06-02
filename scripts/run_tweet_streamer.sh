BASEDIR=$(dirname "$0")
PROJECTDIR="$PWD/$BASEDIR/../"
TWITTER_CREDS_CONFIG_PATH="/etc/config/TweetStreamer/twitter_config.json"
OUTPUT_DIRECTORY="/home/v2john/tweeto"

cd $PROJECTDIR && \
gradle build && \
$PROJECTDIR/build/scripts/tweet-streamer \
-twitter-creds-filepath $TWITTER_CREDS_CONFIG_PATH \
-output-directory $OUTPUT_DIRECTORY

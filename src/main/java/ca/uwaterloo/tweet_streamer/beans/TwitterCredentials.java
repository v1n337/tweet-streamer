package ca.uwaterloo.tweet_streamer.beans;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@ToString
public class TwitterCredentials
{
    private String consumerKey;
    private String consumerSecret;
    private String accessTokenKey;
    private String accessTokenSecret;
}

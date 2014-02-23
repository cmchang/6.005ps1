package twitter;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
/**
 * Filter consists of methods that filter a list of tweets for those matching a condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but you should implement their
 * method bodies, and you may add new public or private methods or classes if you like.
 * 
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets, not modified by this method.
     * @param username
     *            Twitter username
     * @return all tweets in the list whose author is username. Twitter
     *         usernames are case-insensitive, so "rbmllr" and "RbMllr" are
     *         equivalent.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> tweetsByUser = new ArrayList<Tweet>();
        
        for (int x = 0; x < tweets.size(); x++){
            Tweet curTwt = tweets.get(x);
            if(username.toLowerCase().equals(curTwt.getAuthor().toLowerCase())){
                tweetsByUser.add(curTwt);
            }
        }
        return tweetsByUser;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets, not modified by this method.
     * @param timespan
     *            timespan
     * @return all tweets in the list that were sent during the timespan.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> timespanTwts = new ArrayList<Tweet>();
        Date start = timespan.getStart();
        Date end = timespan.getEnd();
        for(Tweet tweet: tweets){
            // conditional checks start <= timespan <= end
            if((start.before(tweet.getTimestamp()) || start.equals(tweet.getTimestamp())) && (end.after(tweet.getTimestamp()) || end.equals(tweet.getTimestamp()))){
                timespanTwts.add(tweet);
            }
        }
        return timespanTwts;
    }

    /**
     * Search for tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. Words must not
     *            contain spaces.
     * @return all tweets in the list such that the tweet text (when represented
     *         as a sequence of words bounded by space characters and the ends
     *         of the string) includes *all* the words found in the words
     *         list, in any order. Word comparison is not case-sensitive, so
     *         "Obama" is the same as "obama".
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> twtsWithWords = new ArrayList<Tweet>();
        boolean wordIsContained;
        for(Tweet tweet: tweets){
            wordIsContained = true;
            for(String word:words){
                if(!tweet.getText().toLowerCase().contains(word.toLowerCase())){
                    wordIsContained = false;
                }
            }
            if(wordIsContained){
                twtsWithWords.add(tweet);
            }
        }
        return twtsWithWords;
    }

}

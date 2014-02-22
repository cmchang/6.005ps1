package twitter;

import java.util.List;
import java.util.Set;
import java.util.Date;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but you should implement their
 * method bodies, and you may add new public or private methods or classes if you like.
 * 
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        Date start = tweets.get(0).getTimestamp();
        Date end = tweets.get(0).getTimestamp();
        
        for(int x = 0; x < tweets.size(); x++){
            Date curTwt = tweets.get(x).getTimestamp();
            if(start.after(curTwt)){
                start = curTwt;
            }
            if(end.before(curTwt)){
                end = curTwt;
            }
        }
        return new Timespan(start, end);
        
    }

    /**
     * Get usernames mentioned in a tweet.
     * 
     * @param tweets
     *            list of tweets, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweet.
     *         A username-mention is "@" followed by a username. A username
     *         consists of letters (A-Z or a-z), digits, and underscores ("_").
     *         Twitter usernames are case-insensitive, so "rbmllr" and "RbMllr"
     *         are equivalent.  A username may occur at most once in the returned 
     *         set.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        throw new RuntimeException("not implemented");
    }

}

package twitter;

import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;

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
        Date start = new Date();
        Date end = start;
        if(tweets.size() > 0){
            start = tweets.get(0).getTimestamp();
            end = tweets.get(0).getTimestamp();
        
            for(int x = 1; x < tweets.size(); x++){//x = 0, already included to initialize start/end
                Date currentTwt = tweets.get(x).getTimestamp();
                if(start.after(currentTwt)){
                    start = currentTwt;
                }
                if(end.before(currentTwt)){
                    end = currentTwt;
                }
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
    public static String validUsernameChars = "abcdefghijklmnopqrstuvwxyz1234567890_";
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<String>();
        
        for(Tweet twt: tweets){
            String currentText = twt.getText();
            while(currentText.contains("@")){
                String user;
                int start = currentText.indexOf("@");
                int end = currentText.indexOf(" ", start);
                

                if(end >= 0){
                    user = currentText.substring(start+1, end); //start+1 cuts off '@'
                }else{ // end == -1, mention was at the end of tweet (no space after username)
                    user = currentText.substring(start+1);
                }
                
                //check if user is an e-mail address
                boolean notEmail;
                if(start > 0){
                    notEmail = (currentText.charAt(start-1) == ' ');
                }else{
                    notEmail = false;
                }
                
                boolean nonemptyUser = user.length() > 0;
                
                boolean validCharsOnly = true;
                for(int i = 0; i < user.length(); i++){
                    if(!validUsernameChars.contains(Character.toString(user.charAt(i)).toLowerCase())){
                        validCharsOnly = false;
                    }
                }
                
                if(nonemptyUser && notEmail && validCharsOnly){
                    mentionedUsers.add(user.toLowerCase());
                }
                currentText = currentText.substring(start+1);
            }
        }
        
        return mentionedUsers;
    }
    
}

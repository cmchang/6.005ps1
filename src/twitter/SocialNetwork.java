package twitter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Comparator;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames.  Users can't follow themselves.
 * If A doesn't follow anybody, then map[A] may be undefined or map[A] may be the
 * empty set; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".  A
 * username should appear at most once as a key in the map or in any given map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but you should implement their
 * method bodies, and you may add new public or private methods or classes if you like.
 * 
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets. One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
        for(Tweet twt: tweets){
            Set<String> usersMentioned = new HashSet<String>();
            usersMentioned =  Extract.getMentionedUsers(Arrays.asList(twt));
            if(usersMentioned.size()>0){
                String author = twt.getAuthor().toLowerCase();
                if(graph.containsKey(author)){
                    for(String user: usersMentioned){
                        graph.get(author).add(user.toLowerCase());
                    }
                }else{
                    graph.put(author, setKeysToLower(usersMentioned));
                }
            }
        }
        return graph;
    }

    //helper function to take in a set of strings and return a matching set of strings (all lowercase)
    private static Set<String> setKeysToLower(Set<String> setOfStr){
        Set<String> setOfLower = new HashSet<String>();
        for(String string: setOfStr){
            setOfLower.add(string.toLowerCase());
        }
        
        return setOfLower; 
    }
    
    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     * 
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        //This map will keep track of all the users and their corresponding number of followers
        Map<String, Integer> followerCount = new HashMap<String, Integer>();
        
        //iterate through followsGraph and keep track of the number followers in follower count
        for(String user: followsGraph.keySet()){
            for(String following: followsGraph.get(user)){
                if(followerCount.containsKey(following)){
                    followerCount.put(following, followerCount.get(following)+1);
                }else{
                    followerCount.put(following, 1);
                }
            }
        }
        
        System.out.println(followerCount);
        
        throw new RuntimeException("not implemented");
    }
    

}

//Helper class so we can sort the users by the number of followers
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
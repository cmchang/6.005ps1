package twitter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.TreeMap;

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
        Map<String, Set<String>> graphMentions = new HashMap<String, Set<String>>();
        for(Tweet twt: tweets){
            Set<String> usersMentioned = new HashSet<String>();
            usersMentioned =  Extract.getMentionedUsers(Arrays.asList(twt));
            if(usersMentioned.size()>0){
                String author = twt.getAuthor().toLowerCase();
                
                if(graphMentions.containsKey(author)){
                    for(String user: usersMentioned){
                        graphMentions.get(author).add(user.toLowerCase());
                    }
                }else{
                    graphMentions.put(author, setKeysToLower(usersMentioned));
                }
            }
        }
        
        //additional code for problem 4: Uncommon hashtags
        Map<String, Set<String>> graphHashTags = guessFollowsUnpopularHashtag(tweets);
        graph = mergeSocialNetworks(graphHashTags, graphMentions);
   
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
    
    // helper function to take in a Map<String, Set<String>> and return 
    // a Map<String, Set<String>> where all the strings are lowercase
    public static Map<String, Set<String>> mapStrsToLower(Map<String, Set<String>> map){
        
        Map<String, Set<String>> newMap = new HashMap<String, Set<String>>();
        for(String key: map.keySet()){
            Set<String> valuesLowerCase = new HashSet<String>();
            valuesLowerCase = setKeysToLower(map.get(key));
            newMap.put(key.toLowerCase(), valuesLowerCase);
        }
        
        return newMap; 
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Additional functions for part 4:  Uncommon Hashtags
    //  Guess/Assumption: An unpopular hashtag (a hashtag used by at most 3 people) 
    //         is likely used by people who follow each other
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Give two social networks, merge them returning a new social network
     * @param networkA a valid social network
     * @param networkB a valid social network
     * @return a new network combining all the users and who they follow from networkA and networkB
     */
    public static Map<String, Set<String>> mergeSocialNetworks(Map<String, Set<String>> networkA, Map<String, Set<String>> networkB){
        Map<String, Set<String>> graph = networkB;
        
        System.out.println(networkA);
        System.out.println(networkB);

        
        System.out.println(graph);
        for(String user: networkA.keySet()){
            String userLC = user.toLowerCase();
            if(graph.containsKey(userLC)){
                Set<String> following = graph.get(userLC);
                for(String follow: networkA.get(user)){
                    following.add(follow);
                }
                graph.put(userLC, following);
            }else{
                graph.put(userLC, networkA.get(user));
            }
        }
        System.out.println(graph);
        return graph;
    }
    
    /**
     * Guess who follows whom based on the use of unpopular hashtags.  This method makes the assumption that
     * an unpopular hashtag is  a hashtag used by at most 3 people.  
     * 
     * @param tweets
     *              a list of tweets providing the evidence, not modified by this method.
     * @return a social network in which user A, B, and C all mututally follow each other if they are the only ones
     *          who use a specific hashtag.  Or user D and E mutually follow each other if they are the only ones
     *          who use a specific hashtag. 
     */
    public static Map<String, Set<String>> guessFollowsUnpopularHashtag(List<Tweet> tweets){
        Map<String, Set<String>> graph = new HashMap<String, Set<String>>();
        Map<String, Set<String>> hashtagUsers = getHashtagUsers(tweets);
        int unpopular = 3;
        
        for(String hashtag: hashtagUsers.keySet()){
            if(hashtagUsers.get(hashtag).size() <= unpopular){
                //unpopular hashtag
                for(String userA: hashtagUsers.get(hashtag)){
                    for(String userB: hashtagUsers.get(hashtag)){
                        if(userA != userB){
                            if(graph.containsKey(userA)){
                                Set<String> following = graph.get(userA);
                                following.add(userB);
                                graph.put(userA, following);
                            }else{
                                Set<String> following = new HashSet<String>(Arrays.asList(userB));
                                graph.put(userA, following);
                            }
                        }
                    }
                }
                
            }
        }
        return graph;
    }
   
    /**
     * Given a list a tweets, extract all of the hashtags and the corresponding users who use that hashtag
     * 
     * @param tweets
     *              a list of tweets providing the evidence, not modified by this method.
     * @return a map where the key represents each hashtag used in the tweets,
     *              and where the values represents all of the users who use that corresponding hashtag
     */
    private static Map<String, Set<String>> getHashtagUsers(List<Tweet> tweets){
        Map<String, Set<String>> hashtagUsers = new HashMap<String, Set<String>>();

        for(Tweet twt: tweets){
            Set<String> hashtagsMentioned = new HashSet<String>();
            hashtagsMentioned = getMentionedHashtags(Arrays.asList(twt));
            String user = twt.getAuthor();
            for(String hashtag: hashtagsMentioned){
                if(hashtagUsers.containsKey(hashtag)){
                    Set<String> users = hashtagUsers.get(hashtag);
                    users.add(user);
                    hashtagUsers.put(hashtag, users);
                }else{
                    Set<String> users = new HashSet<String>(Arrays.asList(user));
                    hashtagUsers.put(hashtag, users);
                }   
            }
        }
        return hashtagUsers;
    }
    
    
    /**
     * Get hashtags used in a tweet.
     * 
     * @param tweets
     *            list of tweets, not modified by this method.
     * @return the set of hashtags that are used in the text of the tweet.
     */
    public static Set<String> getMentionedHashtags(List<Tweet> tweets) {
        Set<String> mentionedHashtags = new HashSet<String>();
        
        for(Tweet twt: tweets){
            String curText = twt.getText();
            while(curText.contains("#")){
                String hashtag;
                
                int start = curText.indexOf("#");
                int end = curText.indexOf(" ", start);
                if(end >= 0){
                    hashtag = curText.substring(start+1, end);
                }else{ // end == -1, mention was at the end of tweet (no space after username)
                    hashtag = curText.substring(start+1);
                }
                curText = curText.substring(start+1);
                mentionedHashtags.add(hashtag.toLowerCase());
            }
        }
        
        return mentionedHashtags;
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
        
        //iterate through followsGraph and keep track of the users and corresponding number followers in followerCount
        for(String user: followsGraph.keySet()){
            for(String following: followsGraph.get(user)){
                String followingLC = following.toLowerCase();
                if(followerCount.containsKey(followingLC)){
                    followerCount.put(followingLC, followerCount.get(followingLC)+1);
                }else{
                    followerCount.put(followingLC, 1);
                }
            }
        }
        
        //Sort followerCount based on the number of followers
        TreeMap<String, Integer> sortedFollowerCount = new TreeMap<String, Integer>();
        sortedFollowerCount.putAll(followerCount);
        
        //Add the sorted users into a List
        List<String> influencers = new ArrayList<String>();
        for(String user: sortedFollowerCount.keySet()){
            influencers.add(0, user);
        }

        return influencers;
    }
    

}
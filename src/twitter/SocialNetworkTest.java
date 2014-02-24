package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * For the guessFollowsGraph method, I will be testing:
 *      - varying tweet list sizes (=0, =1, >1)
 *      - list of tweets none/one/more than one mentions of others
 *      - mentions of other users with varying lower/upper cases
 *      
 * For the influencers method, I will be testing:
 *      - varying social network sizes (=0, =1, >1)
 *      - social networking with users following one/more than one user
 *      - users with the none/same/varying user counts
 *      - social network with users that have varying lower/upper cases
 */

public class SocialNetworkTest {
    
    private static Date d1;
    private static Date d2;
    private static Date d3;
    private static Date d4;
    private static Date d5;
    
    private static Tweet tweet1;
    private static Tweet tweet2;
    private static Tweet tweet3;
    private static Tweet tweet4;
    private static Tweet tweet5;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 10, 00, 00);
        d1 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 11, 00, 00);
        d2 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 11, 00, 00);
        d3 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 12, 00, 00);
        d4 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 13, 00, 00);
        d5 = calendar.getTime();

        
        //note: tweets 2 and 3 have the same time stamp
        tweet1 = new Tweet(0, "alyssa", "is it reasonable to talk about rivest so much?", d1);
        tweet2 = new Tweet(1, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
        tweet3 = new Tweet(2, "H3LL0", "Hello! @H3LLO_WORLD2016 @world2016 @alyssa", d3);
        tweet4 = new Tweet(3, "world2016", "text4 @h3Llo_world2016", d4);
        tweet5 = new Tweet(5, "h3Llo_world2016", "text5 @BBITdiDdle", d5);

    }
    
    //helper function to take in a set of strings and return a matching set of strings (all lowercase)
    public static Set<String> setKeysToLower(Set<String> setOfStr){
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
    //  Tests for guessFollowsGraph Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Test   // this tests an empty tweet list
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        
        assertTrue(followsGraph.isEmpty());
    }
    
    @Test   // this tests a tweet list of size 1, with one mention of another user
    public void testGuessFollowsGraphOneMention() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5));
        
        assertFalse(followsGraph.isEmpty());
        assertEquals(followsGraph.size(),1);
        assertTrue(setKeysToLower(followsGraph.keySet()).contains("h3llo_world2016"));
        
        Set<String> solns = new HashSet<String>(Arrays.asList("bbitdiddle"));

        assertTrue(mapStrsToLower(followsGraph).get("h3llo_world2016").containsAll(solns));
        
    }
    
    @Test   // this tests a tweet list of size >1, with one and multiple mentions of other users (varying lower/upper cases)
    public void testGuessFollowsGraphMultipleMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5));
        
        assertFalse(followsGraph.isEmpty());
        
        Set<String> keys = new HashSet<String>(Arrays.asList("h3llo_world2016","world2016","h3ll0"));
        assertTrue(setKeysToLower(followsGraph.keySet()).containsAll(keys));
        
        //checks who "h3llo_world2016" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("bbitdiddle"));
        assertTrue(mapStrsToLower(followsGraph).get("h3llo_world2016").containsAll(valuesA));
        
      //checks who "world2016" is following
        Set<String> valuesB = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        assertTrue(mapStrsToLower(followsGraph).get("world2016").containsAll(valuesB));
        
      //checks who "h3llo" is following
        Set<String> valuesC = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "alyssa"));
        assertTrue(mapStrsToLower(followsGraph).get("h3ll0").containsAll(valuesC));
        
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for influencers Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Test //This tests an empty social network
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue(influencers.isEmpty());
        
    }

    @Test //This tests a social network of size one, user following one person
    public void testInfluencersSizeOfOneAndOneFollower() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        Set<String> following = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        followsGraph.put("alyssa", following);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 1);
        assertTrue(influencers.contains("h3llo_world2016"));
        
    }
    
    @Test //This tests a social network of size one, user following more than one person
    public void testInfluencersSizeOneAndMultipleFollowers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        Set<String> following = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "bbitdiddle"));

        followsGraph.put("alyssa", following);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 1);
        assertEquals(influencers.get(0).toLowerCase(), "h3llo_world2016");
        
    }
    
    @Test //This tests a social network of size greater than one, multiple users following the same person, varying lower/upper cases, users with the same/varying number of followers
    public void testInfluencersFollowingSameUser() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        String user1 = new String("alyssa");
        Set<String> following1 = new HashSet<String>(Arrays.asList("h3lLo_world2016", "world2016", "bBitdIDDle"));

        String user2 = new String("bbitdiddle");
        Set<String> following2 = new HashSet<String>(Arrays.asList("h3llo_woRld2016", "world2016"));

        String user3 = new String("h3llo_woRld2016");
        Set<String> following3 = new HashSet<String>(Arrays.asList("world2016"));
        
        followsGraph.put(user1, following1);
        followsGraph.put(user2, following2);        
        followsGraph.put(user3, following3);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 3);
        
        //Expected order of list: world2016 (3 followers), h3llo_world2016 (2 followers), bbitdiddle (1 follower)
        assertEquals(influencers.get(0).toLowerCase(), "world2016");
        assertEquals(influencers.get(1).toLowerCase(), "h3llo_world2016");
        assertEquals(influencers.get(2).toLowerCase(), "bbitdiddle");
        
    }
    
    @Test //This tests a social network of size greater than one, multiple users following the same person
    public void testInfluencersFollowingDifferentUsers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        String user1 = new String("alyssa");
        Set<String> following1 = new HashSet<String>(Arrays.asList("world2016"));

        String user2 = new String("bbitdiddle");
        Set<String> following2 = new HashSet<String>(Arrays.asList("world2016","h3llo_world2016"));

        String user3 = new String("h3llo_woRld2016");
        Set<String> following3 = new HashSet<String>(Arrays.asList("world2016"));
        
        followsGraph.put(user1, following1);
        followsGraph.put(user2, following2);        
        followsGraph.put(user3, following3);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 2);
       
      //Expected order of list: world2016 (3 followers), h3llo_world2016 (2 followers), bbitdiddle (1 follower)
        assertEquals(influencers.get(0).toLowerCase(), "world2016");
        assertEquals(influencers.get(1).toLowerCase(), "h3llo_world2016");

        
    }
}

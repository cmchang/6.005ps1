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
 * Note: I label all of the partition spaces (i.e. A1, B1, B2, etc) so I can explain which
 * which partition spaces I'm testing above each test method.
 * 
 * For the guessFollowsGraph method, I will be testing:
 *      (A) varying tweet list sizes (=0, =1, >1)
 *          (A1) =0, (A2) =1, (A3) >1
 *      (B) list of tweets no/one/more than one mentions of others
 *          (B1) no mentions, (B2) one mention, (B3) more than one mention
 *      (C) mentions of other users with varying lower/upper cases
 *      (D) user mentions him/herself **
 *      
 * For the influencers method, I will be testing:
 *      (A) varying social network sizes (=0, =1, >1)
 *          (A1) =0, (A2) =1, (A3) >1
 *      (B) social networking with users following one/more than one user
 *          (B1) =0, (B2) =1, (B3) >1
 *      (C) users with the same/varying number of follower
 *          (C1) same (C2) varying
 *      (D) social network with users that have varying lower/upper cases
 *      (E) user uses @ sign not followed by a user, e-mail address included
 *      
 * For PROBLEM 4, Get Smarter: Uncommon Hashtags
 * Guess/Assumption: An unpopular hashtag (a hashtag used by at most 3 people) is likely used by people who follow each other
 *      
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
    private static Tweet tweet6;
    private static Tweet tweet7;
    
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

        tweet1 = new Tweet(0, "alyssa", "is it reasonable to talk about rivest so much? #lovemath #MIT", d1);
        tweet2 = new Tweet(1, "bbitdiddle", "rivest talk in 30 minutes #hype #MIT", d2);
        tweet3 = new Tweet(2, "H3LL0", "Hello! #MIT @H3LLO_WORLD2016 @world2016 @alyssa", d3);
        tweet4 = new Tweet(3, "world2016", "text4 #MIT @h3Llo_world2016", d4);
        tweet5 = new Tweet(5, "h3Llo_world2016", "text5 @BBITdiDdle #lovemath", d5);
        tweet6 = new Tweet(6, "h3Llo_world2016", "following self @h3Llo_world2016 #lovemath", d5);
        tweet7 = new Tweet(7, "hardworker", "I'm @ the reading room! hello@mit.edu ", d5);

    }
    

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for guessFollowsGraph Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
 
            // Checks partitions in: (A1), (B1)
    @Test   // this tests an empty tweet list
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        assertTrue(followsGraph.isEmpty());
    }
    
            // Checks partitions in: (A2), (B2)
    @Test   // this tests a tweet list of size 1, with one mention of another user
    public void testGuessFollowsGraphOneMention() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5));
        
        assertFalse(followsGraph.isEmpty());
        assertEquals(followsGraph.size(),1);
        assertTrue(Helper.setOfStrToLowerCase(followsGraph.keySet()).contains("h3llo_world2016"));
        
        Set<String> solns = new HashSet<String>(Arrays.asList("bbitdiddle"));
        
        if(Helper.mapOfStrToLowerCase(followsGraph).containsKey("h3llo_world2016")){
            assertTrue(Helper.mapOfStrToLowerCase(followsGraph).get("h3llo_world2016").containsAll(solns));
        }else{
            fail(); //solution does not contain desired key
        }
        
    }
    
            // Checks partitions in: (A3), (B3), (C)
    @Test   // this tests a tweet list of size >1, with one and multiple mentions of other users (varying lower/upper cases)
    public void testGuessFollowsGraphMultipleMentions() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5));
        assertFalse(followsGraph.isEmpty());
        
        Set<String> keys = new HashSet<String>(Arrays.asList("h3llo_world2016","world2016","h3ll0"));
        assertTrue(Helper.setOfStrToLowerCase(followsGraph.keySet()).containsAll(keys));
        
        //checks who "h3llo_world2016" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("bbitdiddle"));
        if(Helper.mapOfStrToLowerCase(followsGraph).containsKey("h3llo_world2016")){
            assertTrue(Helper.mapOfStrToLowerCase(followsGraph).get("h3llo_world2016").containsAll(valuesA));
        }else{
            fail(); //solution does not contain desired key
        }
        
        //checks who "world2016" is following
        Set<String> valuesB = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        if(Helper.mapOfStrToLowerCase(followsGraph).containsKey("world2016")){
            assertTrue(Helper.mapOfStrToLowerCase(followsGraph).get("world2016").containsAll(valuesB));
        }else{
            fail(); //solution does not contain desired key
        }
        
        //checks who "h3llo" is following
        Set<String> valuesC = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "alyssa"));
        if(Helper.mapOfStrToLowerCase(followsGraph).containsKey("h3ll0")){
            assertTrue(Helper.mapOfStrToLowerCase(followsGraph).get("h3ll0").containsAll(valuesC));
        }else{
            fail(); //solution does not contain desired key
        }
    }
            // Checks partitions in (D)
    @Test   // this tests when a user follows him/herself
    public void testGuessFollowsUserFollowsSelf() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6));
        assertTrue(followsGraph.isEmpty());
    }
            //checks partitions in (E)
    @Test   // this tests when a user uses @ and and e-mail address
    public void testGuessFollowsUserVariousAtSigns() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet7));
        assertTrue(followsGraph.isEmpty());
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for influencers Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
            // Checks partitions in: (A1)
    @Test   //This tests an empty social network
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue(influencers.isEmpty());
        
    }
    
            // Checks partitions in: (A2), (B2)
    @Test   //This tests a social network of size one, user following one person
    public void testInfluencersSizeOfOneAndOneFollower() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        Set<String> following = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        followsGraph.put("alyssa", following);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 1);
        assertTrue(influencers.contains("h3llo_world2016"));
        
    }
    
          // Checks partitions in: (A2), (B3)
    @Test //This tests a social network of size one, user following more than one person
    public void testInfluencersSizeOneAndMultipleFollowers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        Set<String> following = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "bbitdiddle"));
        followsGraph.put("alyssa", following);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 3);
        assertTrue(influencers.containsAll(following));
        
    }
    
            // Checks partitions in: (A3), (B1)/(B2)/(B3), (C1), (D)
    @Test   //This tests a social network of size greater than one, multiple users following the same person, 
            //varying lower/upper cases, users with the same/varying number of followers
            //This also tests if a user is added without following anyone
    public void testInfluencersFollowingSameUser() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        String user1 = new String("alyssa");
        Set<String> following1 = new HashSet<String>(Arrays.asList("h3lLo_world2016", "world2016", "bBitdIDDle"));

        String user2 = new String("bbitdiddle");
        Set<String> following2 = new HashSet<String>(Arrays.asList("h3llo_woRld2016", "world2016"));

        String user3 = new String("h3llo_woRld2016");
        Set<String> following3 = new HashSet<String>(Arrays.asList("world2016"));
        
        String user4 = new String("me");
        Set<String> following4 = new HashSet<String>(); //not following anyone!
        
        followsGraph.put(user1, following1);
        followsGraph.put(user2, following2);        
        followsGraph.put(user3, following3);
        followsGraph.put(user4, following4);
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse(influencers.isEmpty());
        assertEquals(influencers.size(), 3);
        
        //Expected order of list: world2016 (3 followers), h3llo_world2016 (2 followers), bbitdiddle (1 follower)
        if(influencers.size() == 3){
            assertEquals(influencers.get(0).toLowerCase(), "world2016");
            assertEquals(influencers.get(1).toLowerCase(), "h3llo_world2016");
            assertEquals(influencers.get(2).toLowerCase(), "bbitdiddle");
        }else{
            fail(); //influencers does not have the right number of users
        }
        
    }
    
            // Checks partitions in: (A3), (B2)/(B3), (C1)
    @Test   //This tests a social network of size greater than one, multiple users following the same person
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
        if(influencers.size() == 2){
            assertEquals(influencers.get(1).toLowerCase(), "h3llo_world2016");
            assertEquals(influencers.get(0).toLowerCase(), "world2016");
        }else{
            fail(); //influencers does not have the right number of users
        }

        
    }
}

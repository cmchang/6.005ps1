package twitter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class mySocialNetworkTest {
    
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
        tweet1 = new Tweet(0, "alyssa", "is it reasonable to talk about rivest so much? #lovemath #MIT", d1);
        tweet2 = new Tweet(1, "bbitdiddle", "rivest talk in 30 minutes #hype #MIT", d2);
        tweet3 = new Tweet(2, "H3LL0", "Hello! #MIT @H3LLO_WORLD2016 @world2016 @alyssa", d3);
        tweet4 = new Tweet(3, "world2016", "text4 #MIT @h3Llo_world2016", d4);
        tweet5 = new Tweet(5, "h3Llo_world2016", "text5 @BBITdiDdle #lovemath", d5);

    }
    

    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for problem 4: unpopular hashtags
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /*
    * The following tests check the returned value from guessFollowsGraph which makes guesses about who follows
    * whom based on mentions AND hashtags. 
    * 
    * Further down, I have a many more tests to check the method guessFollowsUnpopularHashtag that just considers 
    * the returned social network that only considers hashtags to make a guess about who follows whom.
    * 
    * guessFollowsGraph has a method that merges the social network of guesses from mentions and the social network
    * of guesses from the guessFollowsUnpopularHashtag method - so the guessFollowsUnpopularHashtag method is tested
    * more thoroughly.
    * 
    */
    
    @Test   // This tests all the tweets that I have created - various mentions and various hashtags
    public void testGuessFollowsGraphMentionsAndHashtags() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5));
        assertFalse(followsGraph.isEmpty());
        
        Set<String> keys = new HashSet<String>(Arrays.asList("h3llo_world2016","world2016","h3ll0", "alyssa"));
        assertTrue(helper.setOfStrToLowerCase(followsGraph.keySet()).containsAll(keys));
        
        //checks who "h3llo_world2016" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("bbitdiddle", "alyssa"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("h3llo_world2016").containsAll(valuesA));
        
        //checks who "world2016" is following
        Set<String> valuesB = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("world2016").containsAll(valuesB));
        
        //checks who "h3llo" is following
        Set<String> valuesC = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "alyssa"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("h3ll0").containsAll(valuesC));
    }
    
    @Test   // This tests a list of one tweet which contains one hashtag and one mention
    public void testGuessFollowsGraphMentionsAndHashtagsOneEach() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5));
        assertFalse(followsGraph.isEmpty());

        Set<String> keys = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        assertTrue(helper.setOfStrToLowerCase(followsGraph.keySet()).containsAll(keys));
        
        //checks who "h3llo_world2016" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("bbitdiddle"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("h3llo_world2016").containsAll(valuesA));
    }
    
    /*
    * The following tests check the method guessFollowsUnpopularHashtag which is used to guess who follows whom
    * purely based on the hashtags (this sub method doesn't consider mentions of other users using the @ symbol).
    * 
    */
    
    @Test   //Tests a list of tweets where the same hashtags aren't used by multiple user, each hashtag is unique to each user
    public void testGuessFollowsJustHashTagsOne() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsUnpopularHashtag(Arrays.asList(tweet2,tweet5));
        assertTrue(followsGraph.isEmpty());
    }
    
    @Test   //Test a hastag shared by only 2 users
    public void testGuessFollowsJustHashTagsTwo() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsUnpopularHashtag(Arrays.asList(tweet3, tweet4, tweet5));
        
        assertFalse(followsGraph.isEmpty());
        
        Set<String> keys = new HashSet<String>(Arrays.asList("world2016","h3ll0"));
        assertTrue(helper.setOfStrToLowerCase(followsGraph.keySet()).containsAll(keys));
        
        //checks who "world2016" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("h3ll0"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("world2016").containsAll(valuesA));
        
        //checks who "h3llo" is following
        Set<String> valuesB = new HashSet<String>(Arrays.asList("world2016"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("h3ll0").containsAll(valuesB));
    
    }
    
    @Test   //Test a hastag shared by only 3 users
    public void testGuessFollowsJustHashTagsThree() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsUnpopularHashtag(Arrays.asList(tweet1, tweet2, tweet3));
        
        assertFalse(followsGraph.isEmpty());
        
        Set<String> keys = new HashSet<String>(Arrays.asList("alyssa", "bbitdiddle", "h3ll0"));
        assertTrue(helper.setOfStrToLowerCase(followsGraph.keySet()).containsAll(keys));
        
        //checks who "alyssa" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("bbitdiddle", "h3ll0"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("alyssa").containsAll(valuesA));
        
        //checks who "bbitdiddle" is following
        Set<String> valuesB = new HashSet<String>(Arrays.asList("alyssa", "h3ll0"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("bbitdiddle").containsAll(valuesB));
        
        //checks who "h3llo" is following
        Set<String> valuesC = new HashSet<String>(Arrays.asList("alyssa", "bbitdiddle"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("h3ll0").containsAll(valuesC));
        
    }
    
    @Test   //Test a hastag shared by more than 3 users (considered popular)
    public void testGuessFollowsJustHashTagsPopular() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsUnpopularHashtag(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5));
        
        assertFalse(followsGraph.isEmpty());
        
        Set<String> keys = new HashSet<String>(Arrays.asList("h3llo_world2016", "alyssa"));
        assertTrue(helper.setOfStrToLowerCase(followsGraph.keySet()).containsAll(keys));
        
        //checks who "alyssa" is following
        Set<String> valuesA = new HashSet<String>(Arrays.asList("h3llo_world2016"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("alyssa").containsAll(valuesA));
        
        //checks who "h3llo_world2016" is following
        Set<String> valuesB = new HashSet<String>(Arrays.asList("alyssa"));
        assertTrue(helper.mapOfStrToLowerCase(followsGraph).get("h3llo_world2016").containsAll(valuesB));
        
    }
}

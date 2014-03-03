package twitter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


import org.junit.BeforeClass;
import org.junit.Test;

/*
 * Note: I label all of the partition spaces (i.e. A1, B1, B2, etc) so I can explain which
 * which partition spaces I'm testing above each test method.
 * 
 * For the Extract method, I will be testing:
 *      (A) varying sizes of the list of tweets (=1, =2, >2)
 *          (A1) =1, (A2) =2, (A3), >2
 *      (B) ordered and unordered (by date) list of tweets
 *          (B1) ordered, (B2) unordered
 *      (C) lists with tweets that have the same time stamp and different time stamps
 *          (C1) same timestamp, (C2) different timestamp
 *      
 * For the getMentionedUsers method, I will be testing:
 *      (A) tweets with and without users mentioned
 *          (A1) with users mentioned
 *          (A2) without users mentioned
 *      (B) tweets mentioning same and different users
 *          (B1) same users
 *          (B2) different users
 *          (B) same and different users
 *      (C) with usernames with digits, underscores, and varying in upper/lowercase letters
 *      (D) varying sizes of the lsit of tweets (=1, >1)
 *          (D1) =1, (D2) >1
 *      (E) Tweet contains e-mail address
 *      (F) Tweet contains @ sign not followed by a user
 *      (G) Tweet contains multiple @ signs in a row
 *      (H) Tweet mention ends with punctuation
 */

public class ExtractTest {
    
    private static Date d1;
    private static Date d2;
    private static Date d3;
    private static Date d4;
    private static Date d5;
    private static Date d6;
    private static Date d7;

    
    private static Tweet tweet1;
    private static Tweet tweet2;
    private static Tweet tweet3;
    private static Tweet tweet4;
    private static Tweet tweet5;
    private static Tweet tweet6;
    private static Tweet tweet7;
    private static Tweet tweet8;
    private static Tweet tweet9;
    private static Tweet tweet10;
    
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
        
        calendar.set(2014, 1, 14, 3, 00, 00);
        d6 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 3, 00, 00);
        d7 = calendar.getTime();

        
        //note: tweets 2 and 3 have the same time stamp
        tweet1 = new Tweet(0, "alyssa", "is it reasonable to talk about rivest so much?", d1);
        tweet2 = new Tweet(1, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
        tweet3 = new Tweet(2, "H3LL0", "Hello! @H3LLO_WORLD2016 @world2016 @alyssa", d3);
        tweet4 = new Tweet(3, "world2012", "text4 @h3Llo_world2016", d4);
        tweet5 = new Tweet(5, "h3Llo_world2016", "text5 @BBITdiDdle", d5);
        tweet6 = new Tweet(6, "alyssa", "text6 @bbitdiDdle", d6);
        tweet7 = new Tweet(6, "alyssa", "text6 alyssa@mit.edu", d7);
        tweet8 = new Tweet(6, "alyssa", "text6 @ hello", d7);
        tweet9 = new Tweet(6, "alyssa", "text6 @@@ hihi", d7);
        tweet10 = new Tweet(6, "alyssa", "Hi @world!!", d7);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for Timespan Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
            // Checks partitions in: (A1)
    @Test   //tests for a list with one tweet
    public void testGetTimespanOneTweet() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals(d1, timespan.getStart());

    }
       
            // Checks partitions in: (A2)
    @Test   //tests for a list with two tweets
    public void testGetTimespanTwoTweets() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals(d1, timespan.getStart());
        assertEquals(d2, timespan.getEnd());
    }
            // Checks partitions in: (A3), (B1), (C2)
    @Test   //tests for a list with 4 tweets in an ordered list (all tweets have unique timestamp)
    public void testGetTimespanOrderedList() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet4, tweet5));
        
        assertEquals(d1, timespan.getStart());
        assertEquals(d5, timespan.getEnd());
    }
            // Checks partitions in: (A3), (B2), (C1)
    @Test   //tests for a list with 5 tweets in an unordered list (tweets 2 and 3 have same timestamp)
    public void testGetTimespanUnorderedList() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet4, tweet5, tweet1, tweet3, tweet2));
        
        assertEquals(d1, timespan.getStart());
        assertEquals(d5, timespan.getEnd());
    }
    
//  // Checks partitions in: (A4)
//@Test   //tests for a list with no tweets
//public void testGetTimespanNoTweets() {
//ArrayList emptyArr = new ArrayList();
//Timespan timespan = Extract.getTimespan(emptyArr);
//
//} 
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for GetMentionedUsers Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////

            // Checks partitions in: (B1), (C), (D2)
    @Test   //tests for tweets with mentions of the same users
    public void testGetMentionedUsersSameMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5, tweet6));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("bbitdiddle"));
        
        //This prevents the test case from being implementation specific
        //Converts all names in mentionedUsers to lower case
        Set<String> mentionedUsersLowercase = new HashSet<String>();
        for(String name: mentionedUsers){
            mentionedUsersLowercase.add(name.toLowerCase());
        }
        assertTrue (mentionedUsersLowercase.containsAll(expectedAns));
    }
    

            // Checks partitions in: (B2), (C), (D2)
    @Test   //tests for tweets with mentions of different users
    public void testGetMentionedUsersDiffMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4, tweet5));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("bbitdiddle","h3llo_world2016"));
        
        //This prevents the test case from being implementation specific
        //Converts all names in mentionedUsers to lower case
        Set<String> mentionedUsersLowercase = new HashSet<String>();
        for(String name: mentionedUsers){
            mentionedUsersLowercase.add(name.toLowerCase());
        }
        assertTrue (mentionedUsersLowercase.containsAll(expectedAns));
    }
    
            // Checks partitions in: (B3), (C), (D2)
    @Test   //tests for tweets with mentions of the same and different users
    public void testGetMentionedUsersSameAndDiffMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "alyssa"));

        //This prevents the test case from being implementation specific
        //Converts all names in mentionedUsers to lower case
        Set<String> mentionedUsersLowercase = new HashSet<String>();
        for(String name: mentionedUsers){
            mentionedUsersLowercase.add(name.toLowerCase());
        }
        assertTrue (mentionedUsersLowercase.containsAll(expectedAns));
    }
    
            // Checks partitions in: (A1), (D1)
    @Test   //tests for tweets with one mention of another user
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("bbitdiddle"));
        
        for(String name: mentionedUsers){
            assertTrue (expectedAns.contains(name.toLowerCase()));
        }
    }
    
            // Checks partitions in: (A2), (D1)
    @Test   //tests for tweets with no mentions of other users
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue(mentionedUsers.isEmpty());
    }
    
            // Checks partitions in: (E)
    @Test   //tests for tweets with e-mail address
    public void testGetMentionedUsersEmailAddress() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet7));
        
        assertTrue(mentionedUsers.isEmpty());
    }
    
            // Checks partitions in: (F)
    @Test   //tests for tweets with @ but no user
    public void testGetMentionedUsersAtSignNoUser() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet8));
        
        assertTrue(mentionedUsers.isEmpty());
    }
    
            // Checks partitions in: (G)
    @Test   //tests for tweets multiple @ signs in a row
    public void testGetMentionedUsersMultipleAtSignsInARow() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet9));
        
        assertTrue(mentionedUsers.isEmpty());
    }

            // Checks partitions in: (H)
    @Test   //tests for tweets with a mention that ends with punctuation
    public void testGetMentionedUsersPunctuation() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet10));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("world"));
        
        Set<String> mentionedUsersLowercase = new HashSet<String>();
        for(String name: mentionedUsers){
            mentionedUsersLowercase.add(name.toLowerCase());
        }
        assertFalse(mentionedUsers.isEmpty());
        assertTrue (mentionedUsersLowercase.containsAll(expectedAns));    }    
    
}

package twitter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * For the Extract method, I will be testing:
 *      - varying sizes of the list of tweets (=1, =2, >2)
 *      - ordered and unordered (by date) list of tweets
 *      - lists with tweets that have the same time stamp and different time stamps
 *      
 * For the getMentionedUsers method, I will be testing:
 *      - tweets with and without users mentioned
 *      - tweets mentioning same and different users
 *          - with usernames varying in digits, underscores, and upper/lowercase letters
 */

public class ExtractTest {
    
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
        tweet3 = new Tweet(2, "H3LL0", "Hello! @H3LLO_WOLRD2016 @world2012 @alyssa", d3);
        tweet4 = new Tweet(3, "world2012", "text4 @h3Llo_world2016", d4);
        tweet5 = new Tweet(5, "h3Llo_world2016", "text5 @BBITdiDdle", d5);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for Timespan Method (4)
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Test   //tests for a list with one tweet
    public void testGetTimespanOneTweet() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        
        assertEquals(d1, timespan.getStart());

    }
    
    @Test   //tests for a list with two tweets
    public void testGetTimespanTwoTweets() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals(d1, timespan.getStart());
        assertEquals(d2, timespan.getEnd());
    }

    @Test   //tests for a list with 4 tweets in an ordered list (all tweets have unique timestamp)
    public void testGetTimespanOrderedList() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet4, tweet5));
        
        assertEquals(d1, timespan.getStart());
        assertEquals(d5, timespan.getEnd());
    }
    
    @Test   //tests for a list with 5 tweets in an unordered list (tweets 2 and 3 have same timestamp)
    public void testGetTimespanUnorderedList() {
        
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet4, tweet5, tweet1, tweet3, tweet2));
        
        assertEquals(d1, timespan.getStart());
        assertEquals(d5, timespan.getEnd());
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for GetMentionedUsers Method (3)
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Test   //tests for tweets with mentions of the same and different users
    public void testGetMentionedUsersTwoSameMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("h3llo_world2016", "world2016", "alyssa"));
        assertTrue(mentionedUsers.equals(expectedAns));
    }
    
    @Test   //tests for tweets with one mention of another user
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        Set<String> expectedAns = new HashSet<String>(Arrays.asList("bbitdiddle"));
        assertTrue(mentionedUsers.equals(expectedAns));
    }
    
    @Test   //tests for tweets with no mentions of other users
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue(mentionedUsers.isEmpty());
    }

}

package twitter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * For the writtenBy method, I will be testing:
 *      - no tweets made by desired user
 *      - varying sizes of tweets list (1, >1)
 *      - multiple tweets made by the desired user
 *          - with varying upper/lowercase letters in the username
 * 
 * For the inTimespan method, I will be testing:
 *      - number of tweets contained in the given timespan (0, 1, >1)
 *      - tweets in list not in timespan (before and after)
 *        
 */

public class FilterTest {

    private static Date d1;
    private static Date d2;
    private static Date d3;
    
    private static Tweet tweet1;
    private static Tweet tweet2;
    private static Tweet tweet3;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 10, 00, 00);
        d1 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 11, 00, 00);
        d2 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 15, 00, 00);
        d3 = calendar.getTime();
        
        tweet1 = new Tweet(0, "alyssa", "is it reasonable to talk about rivest so much?", d1);
        tweet2 = new Tweet(1, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
        tweet3 = new Tweet(2, "AlySsa", "text", d3);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for writtenBy Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Test   //None of the tweets are made by the desired user
    public void testWrittenByMultipleNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "idontexist");
        
        assertTrue(writtenBy.isEmpty());
        assertEquals(0, writtenBy.size());
    }
    
    @Test   //exactly one tweet made by the desired user
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertFalse(writtenBy.isEmpty());
        assertEquals(1, writtenBy.size());
        assertTrue(writtenBy.contains(tweet1));
    }
    
    @Test //multiple tweets made by the desired user (username with varying lower/uppercases in different tweets)
    public void testWrittenByMultipleTweetsSameUser() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertFalse(writtenBy.isEmpty());
        assertEquals(2, writtenBy.size());
        assertTrue(writtenBy.contains(tweet1));
        assertTrue(writtenBy.contains(tweet3));

    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for inTimespan Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 9, 00, 00);
        Date testDateStart = calendar.getTime();
        calendar.set(2014, 1, 14, 12, 00, 00);
        Date testDateEnd = calendar.getTime();
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testDateStart, testDateEnd));
        
        assertFalse(inTimespan.isEmpty());
        assertTrue(inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for containing Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse(containing.isEmpty());
        assertTrue(containing.containsAll(Arrays.asList(tweet1, tweet2)));
    }

}

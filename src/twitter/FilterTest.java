package twitter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * Note: I label all of the partition spaces (i.e. A1, B1, B2, etc) so I can explain which
 * which partition spaces I'm testing above each test method.
 * 
 * For the writtenBy method, I will be testing:
 *      (A) tweets made by desired user (none, one, more than one)
 *          (A1) none, (A2) one, (A2) more than one
 *      (B) varying sizes of tweets list (=1, >1)
 *          (B1) =1, (B2) >1
 *      (C) varying upper/lowercase letters in the username
 * 
 * For the inTimespan method, I will be testing:
 *      (A) number of tweets contained in the given timespan (=0, =1, >1)
 *          (A1) =0, (A2) =1, (A3) >1
 *      (B) tweets relative to the timespan (before, after, within)
 *          (B1) before, (B2) after, (B3) within
 * 
 * For the containing method, I will be testing:
 *      - words not in the tweets
 *      - words in the tweets:
 *          - =1, >1
 *      - words in and not in tweets
 *      - case insensitive
 */

public class FilterTest {

    private static Date d1;
    private static Date d2;
    private static Date d3;
    private static Date d4;
    
    private static Tweet tweet1;
    private static Tweet tweet2;
    private static Tweet tweet3;
    private static Tweet tweet4;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 10, 00, 00);
        d1 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 11, 00, 00);
        d2 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 15, 00, 00);
        d3 = calendar.getTime();
        
        calendar.set(2014, 1, 14, 15, 00, 00);
        d4 = calendar.getTime();
        
        tweet1 = new Tweet(0, "alyssa", "is it reasonable to talk about rivest so much?", d1);
        tweet2 = new Tweet(1, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
        tweet3 = new Tweet(2, "AlySsa", "text tALk", d3);
        tweet4 = new Tweet(2, "AlySsa", "text talK", d3);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for writtenBy Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
            // Checks partitions in: (A1), (B1)
    @Test   //None of the tweets are made by the desired user
    public void testWrittenByMultipleNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "idontexist");
        
        assertTrue(writtenBy.isEmpty());
        assertEquals(0, writtenBy.size());
    }
            // Checks partitions in:   (A2), (B2)
    @Test   //exactly one tweet made by the desired user
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertFalse(writtenBy.isEmpty());
        assertEquals(1, writtenBy.size());
        assertTrue(writtenBy.contains(tweet1));
    }
            // Checks partitions in: (A3), (B2), (C)
    @Test   //multiple tweets made by the desired user (username with varying lower/uppercases in different tweets)
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
 
            // Checks partitions in: (A3), (B3)
    @Test   // both tweets within the timespan
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
            // Checks partitions in: (A3), (B2), (B3)
    @Test   // two tweets within timespan, one outside/after timespan 
    public void testInTimespanMultipleTweetsInAndOutTimespan() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 9, 00, 00);
        Date testDateStart = calendar.getTime();
        calendar.set(2014, 1, 14, 12, 00, 00);
        Date testDateEnd = calendar.getTime();
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testDateStart, testDateEnd));
        
        assertFalse(inTimespan.isEmpty());
        assertTrue(inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
    }
    
            // Checks partitions in: (A3), (B2)
    @Test   // all tweets outside of timespan
    public void testInTimespanMultipleTweetsOutOfTimespan() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 7, 00, 00);
        Date testDateStart = calendar.getTime();
        calendar.set(2014, 1, 14, 9, 00, 00);
        Date testDateEnd = calendar.getTime();
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testDateStart, testDateEnd));
        
        assertTrue(inTimespan.isEmpty());
        assertFalse(inTimespan.contains(Arrays.asList(tweet1, tweet2)));
    }
       
            // Checks partitions in: (A2), (B3)
    @Test   // one tweet, in timespan
    public void testInTimespanOneTweetsInTimespan() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 9, 00, 00);
        Date testDateStart = calendar.getTime();
        calendar.set(2014, 1, 14, 12, 00, 00);
        Date testDateEnd = calendar.getTime();
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(testDateStart, testDateEnd));
        
        assertFalse(inTimespan.isEmpty());
        assertTrue(inTimespan.containsAll(Arrays.asList(tweet1)));
    }
        
            // Checks partitions in: (A1), (B1)
    @Test   //one tweet, outside/before timespan
    public void testInTimespanOneTweetsOutOfTimespan() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(2014, 1, 14, 17, 00, 00);
        Date testDateStart = calendar.getTime();
        calendar.set(2014, 1, 14, 18, 00, 00);
        Date testDateEnd = calendar.getTime();
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3), new Timespan(testDateStart, testDateEnd));
        
        assertTrue(inTimespan.isEmpty());
        assertFalse(inTimespan.contains(Arrays.asList(tweet3)));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //  Tests for containing Method
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Test   // word contained in multiple tweets, varied upper/lowercases
    public void testContainingWordInTwts() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("talk"));
        
        assertFalse(containing.isEmpty());
        assertTrue(containing.containsAll(Arrays.asList(tweet1, tweet2, tweet3, tweet4)));
    }

    @Test   // word not contained in tweets
    public void testContainingWordNotInTwts() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("helloworld"));
        
        assertTrue(containing.isEmpty());
        assertFalse(containing.containsAll(Arrays.asList(tweet1, tweet2)));
    }
    
    @Test   // word contained in multiple tweets, varied upper/lowercases
    public void testContainingWordInAndNotInTwts() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("talk", "helloworld"));
        
        assertTrue(containing.isEmpty());
        assertFalse(containing.containsAll(Arrays.asList(tweet1, tweet2, tweet3, tweet4)));
    }
}

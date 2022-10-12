/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2026-02-17T01:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "csdg", "sdaf kluio jm", d3);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "rivest kluio jm", d3);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "rivest talk in sfdjk@mit.edu.cn 30 minutes #hype", d2);
    private static final Tweet tweet6 = new Tweet(6, "skdjfal", "I like @haha and @HAHA", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

    /*
     * Testing strategy for Extract.getTimespan
     *
     * Partition the inputs as follows:
     *  list.length(): 1, > 1
     *  list of timestamp of tweet: distinct, repeated partially, same timestamp
     */

    // This test covers list.length()=1
    @Test
    public void oneTweetGetTimeSpanTest() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    // This test covers list.length()>1, distinct timestamp
    @Test
    public void distinctTimestampGetTimeSpanTest() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    // This test covers list.length()>1, timestamp repeated partially
    @Test
    public void repeatedPartiallyTimestampGetTimeSpanTest() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3, tweet4));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    // This test covers list.length()>1, timestamp same
    @Test
    public void sameTimestampGetTimeSpanTest() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet4));

        assertEquals("expected start", d3, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    /*
     * Testing strategy for Extract.getMentionedUser
     *
     * Partition the inputs as follows:
     *  list.length(): 1, > 1
     *  texts of tweet: mention, not mention && contains email address(like @mit.edu), not mention && not contains email address
     *
     * No need to partition result, since that mention==result.size()>0
     */

    // This test covers list.length()=1, mention
    @Test
    public void oneTweetAndMentionGetMentionedUsersTest() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));

        assertFalse("expected non-empty set", mentionedUsers.isEmpty());
        assertTrue("expected same user", new HashSet<>(Arrays.asList("haha", "HAHA")).containsAll(mentionedUsers));
    }

    // This test covers list.length()=1, not mention && contains email address
    @Test
    public void oneTweetAndNoMentionAndContainEmailAddrGetMentionedUsersTest() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // This test covers list.length()=1, not mention && not contains email address
    @Test
    public void oneTweetAndNoMentionAndNotContainEmailAddrGetMentionedUsersTest() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // This test covers list.length()>1, mention
    @Test
    public void manyTweetAndMentionGetMentionedUsersTest() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6, tweet1));

        assertFalse("expected non-empty set", mentionedUsers.isEmpty());
        assertTrue("expected same user", new HashSet<>(Arrays.asList("haha", "HAHA")).containsAll(mentionedUsers));
    }

    // This test covers list.length()=1, not mention && contains email address
    @Test
    public void manyTweetAndNoMentionAndContainEmailAddrGetMentionedUsersTest() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5, tweet1));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // This test covers list.length()=1, not mention && not contains email address
    @Test
    public void manyTweetAndNoMentionAndNotContainEmailAddrGetMentionedUsersTest() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4, tweet2, tweet3));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
}

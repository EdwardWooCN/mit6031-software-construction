/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;

public class SocialNetworkTest {

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

    /*
     * Testing strategy for SocialNetwork.guessFollowsGraph
     *
     * Partition the result as follows:
     *  result.length(): 0, > 0
     */

    //test covers result==0
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    //test covers result>0
    @Test
    public void testGuessFollowsGraphNonEmptyResult() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6));
        Map<String, Set<String>> expectedResult = new HashMap<>();
        expectedResult.put("skdjfal", new HashSet<>(Arrays.asList("haha")));

        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertEquals("expected graph nodes", followsGraph, expectedResult);
    }

    /*
     * Testing strategy for SocialNetwork.guessFollowsGraph
     *
     * Partition the result as follows:
     *  result.length(): 0, > 0
     */

    //test covers result==0
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    //test covers result>0
    @Test
    public void testInfluencersNonEmptyResult() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("mentioned1")));
        followsGraph.put("user2", new HashSet<>(Arrays.asList("mentioned1", "mentioned2")));
        followsGraph.put("user3", new HashSet<>(Arrays.asList("mentioned3", "mentioned4")));
        followsGraph.put("user4", new HashSet<>(Arrays.asList("mentioned1", "mentioned2", "mentioned3", "mentioned4")));
        followsGraph.put("user5", new HashSet<>(Arrays.asList("mentioned3", "mentioned1", "mentioned5")));
        followsGraph.put("user6", new HashSet<>(Arrays.asList("mentioned1", "mentioned3", "mentioned2")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);

        System.out.println(influencers.toString());

        assertFalse("expected non-empty list", influencers.isEmpty());
        assertEquals("expected in order list", 0, influencers.indexOf("mentioned1"));//size=5
        assertEquals("expected in order list", 1, influencers.indexOf("mentioned3"));// size=4
        assertEquals("expected in order list", 2, influencers.indexOf("mentioned2"));// size=3
        assertEquals("expected in order list", 3, influencers.indexOf("mentioned4"));// size=2
        assertEquals("expected in order list", 4, influencers.indexOf("mentioned5"));// size=1
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}

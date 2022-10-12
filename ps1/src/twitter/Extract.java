/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.*;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        Instant startInstant = null, endInstant = null;
        for (Tweet tweet : tweets) {
            if (Objects.isNull(tweet.getTimestamp())) {
                continue;
            }
            if (Objects.isNull(startInstant) || tweet.getTimestamp().compareTo(startInstant) < 0) {
                startInstant = tweet.getTimestamp();
            }
            if (Objects.isNull(endInstant) || tweet.getTimestamp().compareTo(endInstant) > 0) {
                endInstant = tweet.getTimestamp();
            }
        }
        if (Objects.isNull(startInstant) || Objects.isNull(endInstant)) {
            throw new NullPointerException("size of tweets is zero");
        }
        return new Timespan(startInstant, endInstant);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> result = new HashSet<>();
        for (Tweet tweet : tweets) {
            result.addAll(getMentionUsersFromText(tweet.getText()));
        }
        return result;
    }

    public static Set<String> getMentionUsersFromText(String text) {
        Set<String> result = new HashSet<>();
        if (Objects.isNull(text)) {
            return result;
        }

        String[] segmentsSplitBySpace = text.split(" ");
        for (String segmentSplitBySpace : segmentsSplitBySpace) {
            String[] segmentSplitByAt = segmentSplitBySpace.split("@");
            if (segmentSplitByAt.length == 2 && segmentSplitByAt[0].length() == 0) {
                result.add(segmentSplitByAt[1]);
            }
        }
        return result;
    }
}

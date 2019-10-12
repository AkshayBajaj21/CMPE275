# CMPE275 Lab-aop-tweet-stat requirements
Enterprise Application Development

CMPE 275 Section 1, Fall 2019
Lab 1 - Aspect Oriented Programming
Status: published
Last updated: 10/11/2019

In this lab, you implement the retry and stats concerns to a tweeting service through Aspect Oriented Programming (AOP). Please note this is an individual assignment.

The tweet service is defined as follows:

package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;

public interface TweetService {
	/**
	 * @throws IllegalArgumentException if the message is more than 140 characters
	 *                                  as measured by string length, or any
	 *                                  parameter is null or empty.
	 * @throws IOException              if there is a network failure.
	 * @returns a unique message ID
	 */
	int tweet(String user, String message) throws IllegalArgumentException, IOException;

	/**
	 * Retweets a message with the given message ID. The given user must be
	 * currently successfully following the current sender of the message in order
	 * to retweet it. As a special case, one is allowed to retweet his own message,
	 * which will end of with a different message ID.
	 * 
	 * 
	 * @throws IllegalArgumentException if any parameter is null or empty.
	 * @throws IOException              if there is a network failure.
	 * @throws AccessControlException   if the given user is not following the
	 *                                  sender of the given message, or the sender
	 *                                  has blocked the given user, or the given
	 *                                  message does not exist.
	 * @returns a unique message ID, different from given messageId parameter.
	 */
	int retweet(String user, int messageId) throws AccessControlException, IllegalArgumentException, IOException;

	/**
	 * If Alice follows Bob, and Bob has not blocked Alice before, any future
	 * message that Bob tweets or retweets after this are shared with Alice. If at
	 * any point Bob blocks Alice, the sharing after blocking will be stopped.
	 * 
	 * @throws IllegalArgumentException if either parameter is null or empty, or
	 *                                  when a user attempts to follow himself.
	 * @throws IOException              if there is a network failure.
	 */
	void follow(String follower, String followee) throws IllegalArgumentException, IOException;

	/**
	 * @throws IllegalArgumentException if either parameter is null or empty, or
	 *                                  when a user attempts to block himself.
	 * @throws IOException              if there is a network failure.
	 */
	void block(String user, String follower) throws IOException, IOException;

}

Since network failures happen relatively frequently, you are asked to implement the crosscutting concern to automatically retry for up to three times for network failures (indicated by an IOException). (Please note the three retries are in addition to the original failed invocation, i.e., a total of four invocations should take place before an IOException is eventual thrown. ) 

You are also asked to implement the following TweetStatsService:
package edu.sjsu.cmpe275.aop.tweet;

public interface TweetStatsService {
   	/**
	 * Reset all the four measurements. For the purposes of this lab, it also resets the
	 * following and blocking records as if the system is starting fresh for any
	 * purpose related to the metrics below.
	 */
	void resetStatsAndSystem();

	/**
	 * @returns the length of longest message a user successfully sent since the
	 *          beginning or last reset. If no messages were successfully tweeted,
	 *          return 0.
	 */
	int getLengthOfLongestTweet();

	/**
	 * @returns the user who is being followed by the biggest number of different
	 *          users since the beginning or last reset. If there is a tie, return
	 *          the 1st of such users based on alphabetical order. If any follower
	 *          has been blocked by the followee, this follower Still count; i.e.,
	 *          Blocking or not does not affect this metric. If someone follows
	 *          him/herself, it does not count. If no users are followed by
	 *          anybody, return null.
	 */
	String getMostFollowedUser();

	/**
	 * @returns the message that has been shared with the biggest number of
	 *          different followers when it is successfully tweeted. If the same
	 *          message (based on string equality) has been tweeted more than once,
	 *          it is considered as different message in each tweeting for this
	 *          purpose, hence the numbers of followers for different tweeting
	 *          actions will not be added together. 
             *         Retweet counts for this purpose as well; i.e., if a message is shared with two
             *         and one of retweets the message, which gets shared with another three users,
              *       then the total sharing is five. Retweet of retweet counts too. 
             *        If there is a tie in number of different followers, return the message in
             *       dictionary order. If no shared messages, return null. 
             *        The very original sender him/herself of a message will NOT be counted 
             *        toward the number of shared users. 
	 */
	String getMostPopularMessage();

	/**
	 * The most productive user is determined by the total length of all the
	 * messages successfully tweeted since the beginning or last reset. If there is
	 * a tie, return the 1st of such users based on alphabetical order. If no users
	 * successfully tweeted, return null. Retweet does not count for this purpose.
	 * 
	 * @returns the most productive user.
	 */
	String getMostProductiveUser();

}

You are expected to implement the above mentioned concerns / features in: PermissionAspect.java, RetryAspect.java, StatsAspect.java, and TweetStatsServiceImpl.java.
For example, the permission check for retweet needs to be done through PermissionAspect.

You do not need to worry about multi-threading; i.e., you can assume invocations on the tweet service and stats service will come from only one thread.

W.r.t. follow and block, the two actions do not directly interfere with each other, i.e., Alice can block Bob, and after that Bob can still follow Alice. The end effect, however, is that when Alice sends a tweet, Bob cannot receive it, since he has been blocked. Both follow and block get cleared upon system reset.




package edu.sjsu.cmpe275.aop.tweet;

public class TweetStatsServiceImpl implements TweetStatsService {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */
	public static int lengthOfLongestTweet=0;
	public static String mostProductiveUser=null;
	public static String MostFollowedUser=null;
	public static String getMostPopularMessage=null;
	public void resetStatsAndSystem() {
	}
    
	public int getLengthOfLongestTweet() {
		return lengthOfLongestTweet;
	}

	public String getMostFollowedUser() {
		return MostFollowedUser;
	}

	public String getMostPopularMessage() {
		return getMostPopularMessage;
	}
	
	public String getMostProductiveUser() {
		return mostProductiveUser;
	}
}




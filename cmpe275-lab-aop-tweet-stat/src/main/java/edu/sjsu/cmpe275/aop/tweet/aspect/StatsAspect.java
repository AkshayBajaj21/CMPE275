package edu.sjsu.cmpe275.aop.tweet.aspect;
import java.security.AccessControlException;
import java.util.*;
import java.util.TreeMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect 
@Order(0)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	static TreeMap <String, ArrayList> map = new TreeMap <String, ArrayList>(); 
	static TreeMap <String, ArrayList> followMap = new TreeMap <String, ArrayList>(); 
	static TreeMap <String, ArrayList> blockMap = new TreeMap <String, ArrayList>(); 
	static TreeMap <String, ArrayList> blockFollowRemove = new TreeMap <String, ArrayList>(); 
	static TreeMap <Integer, String> tweetIdMap = new TreeMap <Integer, String>() ;
    static TreeMap <Integer, String> tweetAuthor = new TreeMap <Integer, String>();
 	static TreeMap <Integer, HashSet<String>> tweetViewers = new TreeMap <Integer, HashSet<String>>(); 
	static TreeMap <Integer, Integer> tweetParent=new TreeMap <Integer, Integer>();
	static TreeMap <String, ArrayList> tweetsOwner = new TreeMap <String, ArrayList>(); 
	

//	@Autowired TweetStatsServiceImpl stats;
	
	@AfterReturning(pointcut="execution(* *.tweet(..))", returning = "result")
	public void postTweet(JoinPoint joinPoint,Object result){
//		System.out.println("Akshay : entered in after tweet aspect to save tweet");
		Object[] args=(Object[])joinPoint.getArgs();
		ArrayList<String> followers= new ArrayList<String>();
		ArrayList<String> followers2= new ArrayList<String>();
		String username = (String)args[0];
		String message = (String)args[1];
		int messageID =  Integer.parseInt(result.toString());
		
		if(message.length() >= 140) {
//			RetryAspect.errorCount =1;
			throw new IllegalArgumentException("Message cannnot be more than 140 characters");
		}
		if(username == null || username.length() == 0||message == null || message.length() == 0  ) {
			throw new IllegalArgumentException("Parameters passed Invalid!!");
		}
			
//		System.out.println("Akshay : "+ username +" entered in after tweet aspect to save tweet and tweet is "+message);
		if(!map.containsKey(username)) {
			ArrayList<String> firstMessage=new ArrayList<String>(1);
			firstMessage.add(message);
			map.put(username, firstMessage);			
		tweetIdMap.put( messageID,message);
		tweetAuthor.put(messageID, username);
		if(followMap.get(username)!=null) {
		followers=followMap.get(username);
		HashSet<String> followersSet=new HashSet<String>();
		followersSet.addAll(followers);
		tweetViewers.put(messageID,followersSet);
//		if(tweetsOwner.containsKey(username)) {
//			System.out.println("enter 1");
//			tweetsOwner.get(username).add(messageID);
//			
//		}
//		else {
//			System.out.println("enter 2");
//			ArrayList<Integer> list=new ArrayList<Integer>(1);
//			list.add(messageID);
//			tweetsOwner.put(username,list);
//		}
		}
		}
		else {
		map.get(username).add(message);
		tweetIdMap.put( messageID,message);
		tweetAuthor.put(messageID, username);
		if(followMap.get(username)!=null) {
			followers2=followMap.get(username);
			HashSet<String> followersSet2=new HashSet<String>();
			followersSet2.addAll(followers2);
//			tweetViewers.get(username).clear();
			tweetViewers.put(messageID,followersSet2);
//			if(tweetsOwner!=null) {
//				tweetsOwner.get(username).add(messageID);
//			}
//			else {
//				System.out.println("enter 4");
//				ArrayList<Integer> list=new ArrayList<Integer>(1);
//				list.add(messageID);
//				tweetsOwner.put(username,list);
//			}
//			System.out.println("Akshay : followers are  id for tweet is "+tweetViewers.get(messageID));
//			 System.out.println("entered retweet 84 and message id is is "+messageID);
		}

		
		} 
	} 
	@Before("execution (public int getLengthOfLongestTweet())")
	public void LongestTweet(JoinPoint joinPoint) {
		int maxLength=0;
		int length=0;
		for(Map.Entry<String,ArrayList> entry : map.entrySet()) {
		  String key = entry.getKey();
		  ArrayList<String> value = entry.getValue();
		  for(int i=0;i<value.size();i++) {
			   length=value.get(i).length();
			  if(length>maxLength) {
				  maxLength=length;  
			  }
		  } 
			}
//		System.out.println("Akshay : length of longest string is "+maxLength);
		TweetStatsServiceImpl.lengthOfLongestTweet=maxLength;
	}


	@Before("execution (public String getMostProductiveUser(..) )")
	public void getMostProductiveUser() {
		int maxSum=0;
		String productiveUser=null;
		for(Map.Entry<String,ArrayList> entry : map.entrySet()) {
			  int sum=0;
			  String key = entry.getKey();
			  ArrayList<String> value = entry.getValue();
			  for(int i=0;i<value.size();i++) {
//				  if(value.get(i).startsWith("<Akshay:Retweet>") == false)
                   sum+=value.get(i).length();;
			  }
			  if(maxSum<sum) {
				  maxSum=sum;
				  productiveUser=key;
			  }
				TweetStatsServiceImpl.mostProductiveUser=productiveUser;
			  }
		
	}
	@Before("execution (public String getMostPopularMessage())")
	public void mostPopularMessage(JoinPoint joinPoint) {
		int maxSum=0;
		String popularMessage=null;
		for(Map.Entry<Integer,HashSet<String>> entry : tweetViewers.entrySet()) {
			  int sum=0;			  
			  int id = entry.getKey();
			  String owner = tweetAuthor.get(id);
			  tweetViewers.get(id).remove(owner);
			  sum = tweetViewers.get(id).size();
			  if(sum>maxSum) {
				  popularMessage=tweetIdMap.get(id);
				  maxSum=sum;
			  }
				TweetStatsServiceImpl.getMostPopularMessage=popularMessage;
			  }
	}
	
	@AfterReturning("execution (public void follow(..))")
	public void beforeFollow(JoinPoint joinpoint){ 
		  Object args [] =  (Object[]) joinpoint.getArgs();
		   String follower=(String) args[0];
		   String followee=(String) args[1];
		   if(follower == null || follower.length()==0||followee ==null||followee.length() == 0 ||follower == followee ) {
			   throw new IllegalArgumentException("Parameters passed Invalid!!"); 
		   }

		   if(!followMap.containsKey(followee)) {

				ArrayList<String> firstFollower=new ArrayList<String>(1);
				firstFollower.add(follower);
			   followMap.put(followee, firstFollower);
			  
		   }
		   else {
			   followMap.get(followee).add(follower);
		   }
//		   System.out.println("follwers are of alex "+followMap.get("alex"));
//		   System.out.println("followers of aman "+followMap.get("aman"));
	}
	
	@AfterReturning("execution (public void block(..))")
	public void beforeBlock(JoinPoint joinpoint){ 
		  Object args [] =  (Object[]) joinpoint.getArgs();
		   String user=(String) args[0];
		   String followee=(String) args[1];
		   if(user == null || user.length()==0||followee ==null||followee.length() == 0 ||user == followee ) {
			   throw new IllegalArgumentException("Parameters passed Invalid!!"); 
		   }
		   if(!blockMap.containsKey(user)) {
				ArrayList<String> firstBlock=new ArrayList<String>(1);
				firstBlock.add(followee);
				ArrayList<Integer> msgID=new ArrayList<Integer>();
				blockMap.put(user, firstBlock);
				if(followMap.get(user)!=null) {
					followMap.get(user).remove(followee);
					if(blockFollowRemove.get(user)!=null) {
						blockFollowRemove.get(user).add(followee);
					}
					else {
						ArrayList<String> list=new ArrayList<String>(1);
						list.add(followee);
						blockFollowRemove.put(user, list);
					}
					
				}
//				System.out.println("line 201 ");
//				if(tweetsOwner!=null) {
//					msgID= tweetsOwner.get(user);
//					System.out.println("line 201 "+msgID);
//		 			for(int i=0;i<msgID.size();i++) {
////		 				System.out.println("before block viewer is "+tweetViewers.get(msgID));
//		 				if(tweetViewers.containsKey(msgID)) {
//		 					
//		 					tweetViewers.remove(followee);	
//		 				}
//                 		
////		 				System.out.println("after block viewer is "+tweetViewers.get(msgID));
//		 			}
//				}

//				tweetViewers.get(key);
//			   System.out.println("Akshay : entered first block and user "+user+" blocked "+followee );
		   }
		   else {
			   blockMap.get(user).add(followee);
				if(followMap.get(user)!=null) {
					followMap.get(user).remove(followee);
					if(blockFollowRemove.get(user)!=null) {
						blockFollowRemove.get(user).add(followee);
					}
					else {
						ArrayList<String> list=new ArrayList<String>(1);
						list.add(followee);
						blockFollowRemove.put(user, list);
					}
				}
//				ArrayList<Integer> msgID=new ArrayList<Integer>() ;
//	 			if(tweetsOwner!=null) {
//	 				msgID= tweetsOwner.get(user);
//		 			for(int i=0;i<msgID.size();i++) {
////		 				System.out.println("before block viewer is "+tweetViewers.get(msgID));
//		 				tweetViewers.get(msgID).remove(followee);
////		 				System.out.println("after block viewer is "+tweetViewers.get(msgID));
//		 			}
//	 			}
		   }
	}
	
	@Before("execution (public String getMostFollowedUser(..) )")
	public void getMostFollowedUser() {
		int maxCount=0;
		String key=null;
		String mostFollowedUser=null;
		int count =0;
		int countFollowBlocked=0;
		for(Map.Entry<String,ArrayList> entry : followMap.entrySet()) {
			 key = entry.getKey();
			 if(blockFollowRemove.get(key)!=null) {
				 countFollowBlocked=blockFollowRemove.get(key).size(); 
			 }
			
			 count = followMap.get(key).size()+countFollowBlocked;
			 if(count>maxCount) {
				 maxCount=count;
				 mostFollowedUser=key;
			 }
//			ArrayList<String> value = entry.getValue();
//			  for(int i=0;i<value.size();i++) {
//
//				  count=value.get(i).length();
////				  System.out.println("Akshay : count for most popular user is "+count);
//				  if(count>maxCount) {
//					  maxCount=count;  
//					  mostFollowedUser=key;
//					  System.out.println("Akshay : count for most popular user is "+maxCount);
//					  System.out.println("Akshay : count for most popular user is "+key);
//				  }
//			  } 
		}
		TweetStatsServiceImpl.MostFollowedUser=mostFollowedUser;
	}
	
	@After("execution (public void resetStatsAndSystem()) ")
	public void clearData(JoinPoint joinpoint){
		
		TweetStatsServiceImpl.lengthOfLongestTweet = 0;
		TweetStatsServiceImpl.mostProductiveUser=null;
		TweetStatsServiceImpl.MostFollowedUser=null;
		TweetStatsServiceImpl.getMostPopularMessage=null;
		map.clear();
		followMap.clear();
		blockMap.clear();
		blockFollowRemove.clear();
		tweetIdMap.clear();
		tweetAuthor.clear();
		tweetViewers.clear();
		tweetParent.clear();
		tweetsOwner.clear();
		
	}
	
	@AfterReturning(pointcut ="execution(* *.retweet(..))",returning = "result")
	public void reTweet(JoinPoint joinPoint,Object result){
//		System.out.println("Akshay : entered in after tweet aspect to save tweet");
		Object[] args=(Object[])joinPoint.getArgs();
		ArrayList<String> followers= new ArrayList<String>();
//		ArrayList<String> followers2= new ArrayList<String>();
		String owner =null;
		String username = (String)args[0];
		int messageID = (Integer)args[1];
		int retweetID = Integer.parseInt(result.toString());
//		System.out.println("Akshay : message id in retweet is "+messageID);
		if(tweetIdMap.containsKey(messageID)) {
			 owner = tweetAuthor.get(messageID);
			if(username == null || username.length()==0|| messageID ==0) {
				throw new IllegalArgumentException("Parameters passed Invalid!!"); 
			}
			if(blockMap.get(owner) != null) {
				if(blockMap.get(owner).contains(username)) {
                  throw new AccessControlException("User is blocked hence cannot retweet");
				}	}			
				
				//sucessful retweer
				if(followMap.get(owner)!=null || username.equals(owner)) {
					   if(!tweetViewers.containsKey(messageID)) {
							if(followMap.get(username)!=null) {
								followers=followMap.get(username);
								HashSet<String> followersSet=new HashSet<String>();
								followersSet.addAll(followers);
								if(tweetViewers.get(messageID)!=null) {
									tweetViewers.get(messageID).addAll(followersSet);
								}
								else {
									tweetViewers.put(messageID, followersSet);
								}
//								System.out.println("Retweet Akshay : followers are  id for tweet is "+tweetViewers.get(messageID));
								}
//									HashSet<String> firstRetweeter=new HashSet<String>(1);
//									firstRetweeter.add(username);
									tweetParent.put(retweetID, messageID);
//									tweetViewers.put(retweetID, firstRetweeter);
							   }
							   else {
//								   System.out.println("entered retweet 261 and message id is is "+messageID);
								   tweetParent.put(retweetID, messageID);
								   tweetViewers.get(messageID).add(username);
//								   System.out.println("Retweet Akshay : followers are  id for tweet is "+tweetViewers.get(messageID));
							   }
				}
				else {
					throw new AccessControlException("User is not following the owner of tweet hence cannot retweet");
					
				}
			
//			else {
//				if(followMap.size()!=0) {					
//				
//				if(followMap.get(owner).contains(username)) {
//					   if(!tweetViewers.containsKey(messageID)) {
//									HashSet<String> firstRetweeter=new HashSet<String>(1);
//									
//									firstRetweeter.add(username);
//									tweetParent.put(retweetID, messageID);
//									tweetViewers.put(messageID, firstRetweeter);
//
//							   }
//							   else {
//								   tweetParent.put(retweetID, messageID);
//								   tweetViewers.get(messageID).add(username);
//							   }
//				}}
//				else {
//					throw new AccessControlException("User is not following the owner of tweet hence cannot retweet");
//				}
//			}
			
		}
		
		else {
			throw new AccessControlException("tweet does not exist hence you cannot retweet it");
		}
        
		}

	@After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void dummyAfterAdvice(JoinPoint joinPoint) {
		System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		//stats.resetStats();
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		System.out.printf("Before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}
	
	
}

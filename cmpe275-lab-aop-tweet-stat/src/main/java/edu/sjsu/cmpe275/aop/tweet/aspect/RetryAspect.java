package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
@Order(1)
public class RetryAspect {
    /*** 
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */
	public static int errorCount=0; 
	int maxTry=3;
	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.*tweet(..))")
	public int tweetCheck(ProceedingJoinPoint j) throws Throwable{
		Integer result = null;
		try {
			result = (Integer)j.proceed();
			return result;
		}
		catch(IllegalArgumentException exp1){
			errorCount++;
			System.out.println("Tweet length exceeded... Try Again!");
			return result;
		}
		catch(IOException exp2) {
			try {
				result = (Integer)j.proceed();
				return result;
				
			}
			catch(IOException exp3){
				try {
					result = (Integer)j.proceed();
					return result;
				}
				catch(IOException exp4) {
				    try {
				    	
						result = (Integer)j.proceed();
						return result;
				    }
				    catch(IOException exp5) {
				    	errorCount++;
						System.out.println(exp5+" exception occured");
						return result;	
				    }

				}
			}
			
		}
	}
	
	
	 @Around("execution (public void follow (..))")
		public void followCheck(ProceedingJoinPoint j) throws Throwable{
		 Integer result = null;
			try {
				result = (Integer)j.proceed();
			}
			catch(IllegalArgumentException exp1){
				errorCount++;
				System.out.println("Tweet length exceeded... Try Again!");
			}
			catch(IOException exp2) {
				try {
					result = (Integer)j.proceed();
					
				}
				catch(IOException exp3){
					try {
						result = (Integer)j.proceed();
					}
					catch(IOException exp4) {
					    try {
					    	
							result = (Integer)j.proceed();
						
					    }
					    catch(IOException exp5) {
					    	errorCount++;
							System.out.println(exp5+" exception occured");
					
					    }

					}
				}
				
			}
		}
	 
	 @Around("execution (public void block (..))")
		public void blockCheck(ProceedingJoinPoint j) throws Throwable{
		 Integer result = null;
			try {
				result = (Integer)j.proceed();
			}
			catch(IllegalArgumentException exp1){
				errorCount++;
				System.out.println("Tweet length exceeded... Try Again!");
			}
			catch(IOException exp2) {
				try {
					result = (Integer)j.proceed();
					
				}
				catch(IOException exp3){
					try {
						result = (Integer)j.proceed();
					}
					catch(IOException exp4) {
					    try {
					    	
							result = (Integer)j.proceed();
						
					    }
					    catch(IOException exp5) {
					    	errorCount++;
							System.out.println(exp5+" exception occured");
					
					    }

					}
				}
				
			}
		}
	 
	 @Around("execution (public void retweet (..))")
		public void retweetCheck(ProceedingJoinPoint j) throws Throwable{
		 Integer result = null;
			try {
				result = (Integer)j.proceed();
			}
			catch(IllegalArgumentException exp1){
				errorCount++;
				System.out.println("Tweet length exceeded... Try Again!");
			}
			catch(IOException exp2) {
				try {
					result = (Integer)j.proceed();
					
				}
				catch(IOException exp3){
					try {
						result = (Integer)j.proceed();
					}
					catch(IOException exp4) {
					    try {
					    	
							result = (Integer)j.proceed();
						
					    }
					    catch(IOException exp5) {
					    	errorCount++;
							System.out.println(exp5+" exception occured");
					
					    }

					}
				}
				
			}
		}
	
//	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.*tweet(..))")
//	public int dummyAdviceOne(ProceedingJoinPoint joinPoint) throws Throwable {
//		System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//		Integer result = null;
//		try {
//			result = (Integer) joinPoint.proceed();
//			System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), result);
//		} catch (Throwable e) {
//			e.printStackTrace();
//			System.out.printf("Aborted the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//			throw e;
//		}
//		return result.intValue();
//	}

} 
 
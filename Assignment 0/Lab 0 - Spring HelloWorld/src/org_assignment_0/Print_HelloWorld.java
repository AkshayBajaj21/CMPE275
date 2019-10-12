package org_assignment_0;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org_assignment_0.Greeting;

public class Print_HelloWorld {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		Greeting greeting = (Greeting) context.getBean("greeting");
		greeting.print();
	}

}

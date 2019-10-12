package org_assignment_0;

public class Greeting {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void print() {
		System.out.println("Hello world from "+getName());
	}
}

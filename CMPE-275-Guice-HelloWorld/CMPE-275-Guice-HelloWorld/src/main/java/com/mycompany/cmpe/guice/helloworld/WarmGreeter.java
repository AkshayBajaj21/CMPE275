/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cmpe.guice.helloworld;

/**
 *
 * @author appleapple
 */
public class WarmGreeter implements Greeter{
    
    String name;
    
    public WarmGreeter(){
        this("Nihal");
    }
    
    public WarmGreeter(String name){
        this.name = name;
    }
    
    public void greet(){
        System.out.println("Hello, my dear World. I am "+this.name+". Nice to see you!");
    }
    
}


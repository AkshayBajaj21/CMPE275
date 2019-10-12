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
public class CoolGreeter implements Greeter{
    
    String name;
    
    public CoolGreeter(){
        this("Nihal");
    }
    
    public CoolGreeter(String name){
        this.name = name;
    }
    
    public void greet(){
        System.out.println("Hey World. Me "+this.name);
    }
    
}

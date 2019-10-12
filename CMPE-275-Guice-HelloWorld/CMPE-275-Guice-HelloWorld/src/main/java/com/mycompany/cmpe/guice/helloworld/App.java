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
import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;

/**
 * Guice HelloWorld!
 *
 */
public class App 
{
	@Inject 
	@Named("CoolGreeter")
	private Greeter coolGreeter;
	@Inject 
	@Named("WarmGreeter")
	private Greeter warmGreeter;
	
    public static void main( String[] args ){
    	App app = new App();
        Module module = new GreeterModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(app);//injects the implementation of the service
        
        app.coolGreeter.greet();
        app.warmGreeter.greet();
    }
}

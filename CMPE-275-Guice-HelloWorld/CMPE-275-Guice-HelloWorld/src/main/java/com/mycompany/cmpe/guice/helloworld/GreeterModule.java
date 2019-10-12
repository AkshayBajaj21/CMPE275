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
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class GreeterModule extends AbstractModule{
    protected void configure() {
        bind(Greeter.class).annotatedWith(Names.named("CoolGreeter")).toInstance(new CoolGreeter("Konda"));
        bind(Greeter.class).annotatedWith(Names.named("WarmGreeter")).to(WarmGreeter.class);
    }    
}

package com.gabry.akkapatterns;

/**
 * Hello world!
 *
 */
public class OOPInvoke {
    public static void main( String[] args ) {
        HelloWorld person1 = new HelloWorld("Person1");
        HelloWorld person2 = new HelloWorld("Person2");
        person1.sayHello(person2,"Hello world");
    }
}

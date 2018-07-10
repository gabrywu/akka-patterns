package com.gabry.akkapatterns;

/**
 * Created by gabry on 2018/7/4 16:39
 */
public class HelloWorld {
    private String name = "";
    public HelloWorld(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void sayHello(HelloWorld to, String msg){
        System.out.println(to.getName()+" 收到 "+name+" 的消息："+ msg);
    }
}

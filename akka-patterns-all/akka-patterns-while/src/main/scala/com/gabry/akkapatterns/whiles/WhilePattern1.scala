package com.gabry.akkapatterns.whiles

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/17 9:10
  */
class WhileActor extends Actor{
  override def receive: Receive = {
    case "start" =>
      println("do start work")
      var i=0
      val maxLine = 10
      while(i<maxLine){
        println(i)
        i += 1
      }
      println("start work done")
    case "others" =>
      println("do other work")
  }
}
object WhilePattern1 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("WhilePattern1",ConfigFactory.load())
    val whileActor = system.actorOf(Props(new WhileActor),"WhilePattern1")
    whileActor ! "start"
    whileActor ! "others"
  }
}

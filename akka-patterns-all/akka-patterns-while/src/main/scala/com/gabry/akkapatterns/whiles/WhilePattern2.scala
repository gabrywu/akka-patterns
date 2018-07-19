package com.gabry.akkapatterns.whiles

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/17 9:31
  */
class WhileForeverActor extends Actor{
  var condition = true
  override def preStart(): Unit = {
    super.preStart()
    self ! "doWork"
  }
  override def receive: Receive = {
    case "doWork" =>
      println("从数据库获取n条数据循环处理")
      Thread.sleep(1000)
      println("处理完毕")
      if(condition){
        self ! "doWork"
      }
    case "otherWork" =>
      println("处理其他消息")
  }
}
object WhilePattern2 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("WhilePattern2",ConfigFactory.load())
    val whileActor: ActorRef = system.actorOf(Props(new WhileForeverActor),"WhilePattern2")
    Thread.sleep(3*1000)
    whileActor ! "otherWork"
    whileActor ! "otherWork"
  }
}

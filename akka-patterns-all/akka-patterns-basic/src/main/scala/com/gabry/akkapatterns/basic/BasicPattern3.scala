package com.gabry.akkapatterns.basic

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.gabry.akkapatterns.basic.protocol.{DoWork, DoWorkFor, HelloSaid, WorkDone}
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/6 14:26
  */
class HelloActor(name:String) extends Actor {
  private def doWork(message:String,forActor:ActorRef):HelloSaid = {
    println(s"$name 收到 ${forActor.path.name} 的消息 [$message] ，工作进行中...")
    HelloSaid("这是处理后返回的消息")
  }
  override def receive: Receive = {
    case DoWorkFor(message,forActor) =>
      println(s"嗨 ${forActor.path.name} ，我正在为你工作")
      val returnMsg = doWork(message,forActor)
      forActor ! WorkDone(returnMsg.message)
    case WorkDone(message) =>
      println(s"$name 收到了 ${sender().path.name} 的回复消息：[$message]")
  }
}
object BasicPattern3 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("BasicPattern2",ConfigFactory.load())
    val person1 = system.actorOf(Props(new HelloActor("person1")),"personActor1")
    val person2 = system.actorOf(Props(new HelloActor("person2")),"personActor2")
    person2 ! DoWorkFor("Hello World",person1)
  }
}

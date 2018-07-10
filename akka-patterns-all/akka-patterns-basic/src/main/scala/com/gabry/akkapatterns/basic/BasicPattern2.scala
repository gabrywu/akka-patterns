package com.gabry.akkapatterns.basic

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.gabry.akkapatterns.basic.protocol.{DoWork, HelloSaid, SayHello, WorkDone}
import com.typesafe.config.ConfigFactory

/**
 * Hello world!
 *
 */
class HelloWorldActor(other:ActorRef,name:String) extends Actor {
  private def doWork(message:String):HelloSaid = {
    println(s"$name 收到 ${other.path.name} 的消息 [$message] ，工作进行中...")
    HelloSaid("这是处理后返回的消息")
  }
  override def receive: Receive = {
    case DoWork(message) =>
      println(s"嗨 ${other.path.name} ，我正在为你工作")
      val returnMsg = doWork(message)
      other ! WorkDone(returnMsg.message)
    case WorkDone(message) =>
      println(s"$name 收到了 ${sender().path.name} 的回复消息：[$message]")
  }
}
object BasicPattern2 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("BasicPattern2",ConfigFactory.load())
    val person1 = system.actorOf(Props(new HelloWorldActor(null,"person1")),"personActor1")
    val person2 = system.actorOf(Props(new HelloWorldActor(person1,"person2")),"personActor2")
    person2 ! DoWork("Hello World")
  }
}

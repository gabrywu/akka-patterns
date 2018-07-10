package com.gabry.akkapatterns.basic

import akka.actor.{Actor, ActorSystem, Props}
import com.gabry.akkapatterns.basic.protocol.{HelloSaid, SayHello}
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/6 13:55
  */
class SimpleActor(name:String) extends Actor {
  private def doWork(message:SayHello):Unit = {
    println(s"$name 收到 ${message.from.path.name} 的消息 [$message] ，工作进行中... 当前线程号 ${Thread.currentThread().getId}")
  }
  override def receive: Receive = {
    case msg @ SayHello(from,message) =>
      doWork(msg)
      val returnMsg = HelloSaid(s"嗨 ${from.path.name} ,${self.path.name} 收到了 $message 消息")
      println(s"$name 工作结束，准备返回消息[${returnMsg.message}]")
  }
}
object BasicPattern1 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("BasicPattern2",ConfigFactory.load())
    val person1 = system.actorOf(Props(new SimpleActor("person1")),"personActor1")
    println(s"Main thread Id ${Thread.currentThread().getId}")
    person1 ! SayHello(person1,"Hello World 1")
    person1 ! SayHello(person1,"Hello World 2")
  }
}
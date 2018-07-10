package com.gabry.akkapatterns.backend

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.gabry.akkapatterns.backend.protocol.DoWorkInBackend
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/10 16:31
  */
class BackendPattern2Actor extends Actor{
  override def receive: Receive = {
    case cmd @ DoWorkInBackend(message,messageTime) =>
      println(s"BackendPattern2Actor receive command [$message,$messageTime] at ${System.currentTimeMillis()}")
      val backend = context.actorOf(Props(new BackendActor(sender())),s"backend-$messageTime")
      context.watch(backend)
      backend ! cmd
  }
}
class BackendActor(from:ActorRef) extends Actor{
  private def doWorkInLongTimeFor(sender:ActorRef):Unit = {
    println(s"do work for $sender")
    Thread.sleep(3*1000)
    println(s"work done ,you can send result to $sender")
  }
  override def receive: Receive = {
    case DoWorkInBackend(message,messageTime) =>
      println(s"BackendActor Receive DoWorkInBackend command at ${System.currentTimeMillis()}")
      println(s"DoWorkInBackend message is $message,message time is $messageTime")
      doWorkInLongTimeFor(from)
  }
}
object BackendPattern2 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("BackendPattern2",ConfigFactory.load())
    val actor = system.actorOf(Props(new BackendPattern2Actor),"BackendPattern2Actor")
    val cmd1 = DoWorkInBackend("command1",System.currentTimeMillis())
    Thread.sleep(500)
    val cmd2 = DoWorkInBackend("command2",System.currentTimeMillis())
    println(s"cmd1 is [${cmd1.message},${cmd1.messageTime}]")
    println(s"cmd2 is [${cmd2.message},${cmd2.messageTime}]")
    actor ! cmd1
    actor ! cmd2
  }
}

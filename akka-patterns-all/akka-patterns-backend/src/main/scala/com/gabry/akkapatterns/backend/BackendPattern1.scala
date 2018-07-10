package com.gabry.akkapatterns.backend

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.gabry.akkapatterns.backend.protocol.{DoWorkInBackend, DoWorkNotInBackend}
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
 * Hello world!
 *
 */
class BackendPattern1Actor extends Actor {
  implicit val executionContextExecutor: ExecutionContextExecutor = context.system.dispatcher
  private def doWorkInLongTimeFor(sender:ActorRef):Unit = {
    println(s"do work for $sender")
    Thread.sleep(3*1000)
  }
  override def receive: Receive = {
    case DoWorkNotInBackend(message,messageTime) =>
      println(s"BackendPattern1Actor Receive DoWorkNotInBackend command at ${System.currentTimeMillis()}")
      println(s"DoWorkNotInBackend message is $message,message time is $messageTime")
      doWorkInLongTimeFor(sender())
      println("DoWorkNotInBackend command done")
    case DoWorkInBackend(message,messageTime) =>
      println(s"BackendPattern1Actor Receive DoWorkInBackend command at ${System.currentTimeMillis()}")
      println(s"DoWorkInBackend message is $message,message time is $messageTime")
      val from = sender()
      Future{
        doWorkInLongTimeFor(from)
      }
      println("DoWorkInBackend command done")
  }
}
object BackendPattern1{
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("BackendPattern1",ConfigFactory.load())
    val actor = system.actorOf(Props(new BackendPattern1Actor),"BackendPattern1Actor")
    val cmd1 = DoWorkNotInBackend("command1",System.currentTimeMillis())
    Thread.sleep(500)
    val cmd2 = DoWorkNotInBackend("command2",System.currentTimeMillis())
    println(s"cmd1 is [${cmd1.message},${cmd1.messageTime}]")
    println(s"cmd2 is [${cmd2.message},${cmd2.messageTime}]")
    actor ! cmd1
    actor ! cmd2
    Thread.sleep(3*1000)
    val cmd3 = DoWorkInBackend("command3",System.currentTimeMillis())
    Thread.sleep(500)
    val cmd4 = DoWorkInBackend("command4",System.currentTimeMillis())
    println(s"cmd3 is [${cmd3.message},${cmd3.messageTime}]")
    println(s"cmd4 is [${cmd4.message},${cmd4.messageTime}]")
    actor ! cmd3
    actor ! cmd4
  }
}

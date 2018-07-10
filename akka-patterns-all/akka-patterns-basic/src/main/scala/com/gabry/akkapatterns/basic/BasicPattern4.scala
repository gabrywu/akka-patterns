package com.gabry.akkapatterns.basic

import akka.actor.{Actor, ActorSystem, Props}
import com.gabry.akkapatterns.basic.protocol.{DoWork, DoWorkFor, WorkDone}
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/6 14:39
  */
class Master(workerPath:String) extends Actor{
  override def receive: Receive = {
    case DoWork(message) =>
      println(s"master 收到 doWork消息:$message")
      val worker = context.actorSelection( s"/user/$workerPath")
      worker ! DoWorkFor(message,self)
    case WorkDone(message) =>
      println(s"master 收到 ${sender().path.name} 的返回消息 $message")
  }
}
class Worker extends Actor{
  private def doWork(message:String):String = {
    println(s"worker 收到了消息 $message")
    "这里是worker返回消息"
  }
  override def receive: Receive = {
    case DoWorkFor(message,forActor) =>
      val result = doWork(message)
      forActor ! WorkDone(result)

  }
}
object BasicPattern4 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("BasicPattern2",ConfigFactory.load())
    system.actorOf(Props(new Worker()),"workerActor")
    val master = system.actorOf(Props(new Master("workerActor")),"masterWorker")
    master ! DoWork("Hello World")
  }
}

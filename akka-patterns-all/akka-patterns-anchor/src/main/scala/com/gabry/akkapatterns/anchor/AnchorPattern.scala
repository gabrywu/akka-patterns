package com.gabry.akkapatterns.anchor

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Hello world!
 *
 */
class AnchorActor extends Actor{
  override def preStart(): Unit = {
    super.preStart()
    println(s"self=$self,path=${self.path}")
  }
  override def receive: Receive = {
    case any =>
      println(s"Hello $any")
  }
}
object AnchorPattern{
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("AnchorPattern",ConfigFactory.load())
    val anchorActor = system.actorOf(Props(new AnchorActor),"anchorActor")
    anchorActor ! 1
    val anchorActorSelection = system.actorSelection("/user/anchorActor")
    anchorActorSelection ! 2
    system.stop(anchorActor)
    // 等待anchorActor彻底stop
    Thread.sleep(3*1000)
    system.actorOf(Props(new AnchorActor),"anchorActor")
    anchorActor ! 3
    anchorActorSelection ! 4

  }
}

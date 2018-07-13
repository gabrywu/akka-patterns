package com.gabry.akkapatterns.chaining

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive
import com.typesafe.config.ConfigFactory

/**
  * Created by gabry on 2018/7/13 10:20
  */
trait Chained{
  def receive:Receive = Actor.emptyBehavior
}
trait IntReceiveChained1 extends Chained{
  override def receive:Receive = super.receive orElse {
    case i:Int => println(s"IntReceiveChained1 receive Int $i")
  }
}
trait IntReceiveChained extends Chained{
  override def receive:Receive = super.receive orElse {
    case i:Int => println(s"IntReceive receive Int $i")
  }
}
trait StringReceiveChained extends Chained {
  override def receive:Receive = super.receive orElse {
    case i:Int => println(s"StringReceive receive Int $i")
    case s:String => println(s"StringReceive receive String $s")
  }
}
class ChainedActor extends Actor with IntReceiveChained with StringReceiveChained with IntReceiveChained1 {
  override def receive:Receive = super.receive orElse {
    case any =>
      println(s"ChainingActor receive any $any")
  }
}
object ChainingPattern1 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ChainingPattern1",ConfigFactory.load())
    val chainingActor = system.actorOf(Props(new ChainedActor),"ChainedActor")
    chainingActor ! 123
    chainingActor ! "test"
  }
}

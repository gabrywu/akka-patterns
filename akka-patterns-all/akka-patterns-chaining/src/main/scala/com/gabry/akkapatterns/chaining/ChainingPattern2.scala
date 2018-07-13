package com.gabry.akkapatterns.chaining

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive
import com.typesafe.config.ConfigFactory

/**
 * Hello world!
 *
 */
trait Chaining { self => Actor
  private var chainedReceives = List.empty[Receive]
  def registerReceive( newReceive:Receive ): Unit = {
    chainedReceives = newReceive :: chainedReceives
  }
  def receive:Receive = chainedReceives.reduce(_ orElse _)
}
trait IntReceive extends Chaining{
  registerReceive{
    case i:Int => println(s"IntReceive receive Int $i")
  }
}
trait StringReceive extends Chaining {
  registerReceive{
    case s:String => println(s"StringReceive receive String $s")
  }
}
class ChainingActor extends Actor with IntReceive with StringReceive{
}
object ChainingPattern2 {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ChainingPattern2",ConfigFactory.load())
    val chainingActor = system.actorOf(Props(new ChainingActor),"chainingActor")
    chainingActor ! 123
    chainingActor ! "test"
  }
}

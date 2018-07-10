package com.gabry.akkapatterns.basic.protocol

import akka.actor.ActorRef

/**
  * Created by gabry on 2018/7/6 12:48
  */
trait Message
trait Command extends Message
trait Event extends Message
final case class DoWork(message:String) extends Command
final case class DoWorkFor(message:String,forActor:ActorRef) extends Command
final case class WorkDone(message:String) extends Event
final case class SayHello(from:ActorRef,message:String) extends Command
final case class HelloSaid(message:String) extends Event
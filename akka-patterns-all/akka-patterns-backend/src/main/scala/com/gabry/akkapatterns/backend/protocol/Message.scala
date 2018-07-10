package com.gabry.akkapatterns.backend.protocol

/**
  * Created by gabry on 2018/7/10 16:07
  */
trait Message
trait Command extends Message
trait Event extends Message
final case class DoWorkInBackend(message:String,messageTime:Long) extends Command
final case class DoWorkNotInBackend(message:String,messageTime:Long) extends Command

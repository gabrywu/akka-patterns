package com.gabry.akkapatterns.message.protocol

/**
  * Created by gabry on 2018/7/11 16:08
  */
trait Message{
  // 消息创建的时间
  final val at:Long = System.currentTimeMillis()
}
trait Command extends Message
trait Event extends Message

trait MasterCommand extends Command{
  // 定义MasterCommand公有的方法或字段
}

object MasterCommand{
  case class StartWork(message:String) extends MasterCommand
}
trait MasterEvent extends Event{
  // 定义MasterEvent公有的方法或字段
}

object MasterEvent{
  case class WorkStarted(message:String) extends MasterCommand
}

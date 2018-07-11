package com.gabry.akkapatterns.message.protocol

/**
  * Created by gabry on 2018/7/11 16:37
  */
trait Message{
  // 消息创建的时间
  final val at:Long = System.currentTimeMillis()
}
trait Command extends Message
trait Event extends Message

trait MasterMessage extends Message{
  // 定义Master消息共有的方法或字段
}

trait MasterCommand extends MasterMessage with Command{
  // 定义MasterCommand公有的方法或字段
}
trait MasterEvent extends MasterMessage with Event{
  // 定义MasterEvent公有的方法或字段
}

object MasterCommand{
  case class StartWork(message:String) extends MasterCommand
}

object MasterEvent{
  case class WorkStarted(message:String) extends MasterCommand
}

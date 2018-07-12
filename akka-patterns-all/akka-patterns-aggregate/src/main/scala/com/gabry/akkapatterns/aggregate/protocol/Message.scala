package com.gabry.akkapatterns.aggregate.protocol

/**
  * Created by gabry on 2018/7/12 15:53
  */
trait Message {
  final val at:Long = System.currentTimeMillis
}
trait Command extends Message
trait Event extends Message
trait AggregateCommand extends Command
trait AggregateEvent extends Event
object AggregateCommand{
  case class Aggregate(parallel:Int) extends AggregateCommand
}
trait AggregateBackendCommand extends Command
object AggregateBackendCommand{
  case class Aggregate(index:Int, parallel:Int) extends AggregateBackendCommand
}
trait AggregateBackendEvent extends Event
object AggregateBackendEvent{
  case class WorkDone(result:Long) extends AggregateBackendCommand
}

trait AggregateWorkerEvent extends Event
object AggregateWorkerEvent{
  case class WorkDone(result:Long) extends AggregateWorkerEvent
}
package com.gabry.akkapatterns.aggregate

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.gabry.akkapatterns.aggregate.protocol.{AggregateBackendCommand, AggregateBackendEvent, AggregateCommand, AggregateWorkerEvent}
import com.typesafe.config.ConfigFactory

/**
 * Hello world!
 *
 */
class AggregateMasterActor extends Actor{
  override def receive: Receive = {
    case cmd: AggregateCommand.Aggregate =>
      // 将此次汇总结果汇报给from，为了简化，此处用self替代
      val from = self
      val backendActor = context.actorOf(Props(new AggregateMasterBackendActor(from,cmd.parallel)),s"AggregateMasterBackendActor-${cmd.at}")
      backendActor ! cmd
    case AggregateBackendEvent.WorkDone(sum) =>
      val from = sender()
      println(s"AggregateMasterActor [${self.path.name}] 收到 ${from.path.name} 汇总结果 $sum")
  }
}
class AggregateMasterBackendActor(replyTo:ActorRef,parallel:Int) extends Actor{
  var counter = 0
  var sum = 0L
  override def receive: Receive = {
    case AggregateCommand.Aggregate(_) =>
      println(s"AggregateMasterBackendActor [${self.path.name}] 开始工作,parallel $parallel，工作结果汇总给 ${replyTo.path.name}")
      1 to parallel foreach { i =>
        val worker = context.actorOf(Props(new AggregateWorker(self)),s"AggregateWorker-$i")
        worker ! AggregateBackendCommand.Aggregate(i,parallel)
      }
    case AggregateWorkerEvent.WorkDone(result) =>
      counter += 1
      sum += result
      if(counter == parallel){
        replyTo ! AggregateBackendEvent.WorkDone(sum)
        context.stop(self)
        println(s"AggregateMasterBackendActor [${self.path.name}] 工作结束退出")
      }
  }
}
class AggregateWorker(replyTo:ActorRef) extends Actor{
  def calcResult(index:Int,parallel:Int):Long = index * parallel
  override def receive: Receive = {
    case AggregateBackendCommand.Aggregate(index,parallel) =>
      println(s"AggregateWorker [${self.path.name}] 开始工作 index=$index，工作汇总给 ${replyTo.path.name}")
      val result = calcResult(index,parallel)
      replyTo ! AggregateWorkerEvent.WorkDone(result)
      println(s"AggregateWorker [${self.path.name}] 工作结束退出")
      context.stop(self)
  }
}

object AggregatePattern {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("AggregatePattern",ConfigFactory.load())
    val aggregateMasterActor =  system.actorOf(Props(new AggregateMasterActor),"AggregateMasterActor")
    aggregateMasterActor ! AggregateCommand.Aggregate(3)
  }
}

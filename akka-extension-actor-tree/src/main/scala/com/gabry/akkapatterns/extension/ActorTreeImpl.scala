package com.gabry.akkapatterns.extension

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorIdentity, ActorPath, ActorRef, ExtendedActorSystem, Extension, ExtensionId, Identify, Props}
import akka.util.Timeout

import scala.concurrent.Await

/**
  * Created by gabry on 2018/8/7 18:12
  */
object ActorTreeExtension extends ExtensionId[ActorTreeImpl]{
  override def createExtension(system: ExtendedActorSystem): ActorTreeImpl = ActorTreeImpl(system)
}
object ActorTreeImpl{
  def apply(system: ExtendedActorSystem): ActorTreeImpl = new ActorTreeImpl(system)
}
class ActorTreeImpl private (system:ExtendedActorSystem) extends Extension{
  import akka.pattern._
  private var currentActors:List[ActorPath] = List.empty[ActorPath]
  private val actorTreeSupervisor:ActorRef = system.actorOf(Props(new ActorTreeSupervisor),"actorTreeSupervisor")
  private implicit val timeout = new Timeout(1,TimeUnit.MINUTES)

  def getCurrentActors:List[ActorPath] = {
    val result = Await.result(actorTreeSupervisor ? "getAllActor",timeout.duration)
    if( result != null ){
      currentActors = result.asInstanceOf[List[ActorPath]]
    }
    currentActors
  }
}
class ActorTreeSupervisor extends Actor{
  private var currentActor:List[ActorPath] = List.empty[ActorPath]
  override def preStart(): Unit = {
    val provider = context.system.asInstanceOf[ExtendedActorSystem].provider
    val user = provider.guardian
    val root = provider.systemGuardian

    user ! Identify(null)
    root ! Identify(null)
  }
  override def receive: Receive = {
    case "getAllActor" =>
      sender ! currentActor
    case ActorIdentity(_,Some(child)) =>
      currentActor = child.path :: currentActor
      if(child.path.name !="deadLetters"){
       // println(s"find actor ${child}")
        context.actorSelection(child.path / "*") ! Identify(null)
      }

  }
}
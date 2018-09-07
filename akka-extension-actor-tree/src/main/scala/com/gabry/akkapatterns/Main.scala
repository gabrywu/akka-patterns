package com.gabry.akkapatterns

import akka.actor.{ActorSystem, ExtendedActorSystem}
import com.gabry.akkapatterns.extension.ActorTreeExtension

/**
 * Hello world!
 *
 */
object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("extension").asInstanceOf[ExtendedActorSystem]
    val extendSys = system.asInstanceOf[ExtendedActorSystem]
    val extendSysClass = extendSys.getClass
    val method = extendSysClass.getMethod("printTree")
    method.setAccessible(true)
    val tree = method.invoke(extendSys)
    println(tree)
    val provider = extendSys.provider
    val providerClass = provider.getClass
    println(s"providerClass=$providerClass,fields=${providerClass.getDeclaredFields.length}")
    providerClass.getDeclaredFields.foreach(_.getName)
    val actorTree = ActorTreeExtension(system)
    Thread.sleep(1000)
    println(s"length=${actorTree.getCurrentActors.length}")
    actorTree.getCurrentActors.foreach(println)
  }
}

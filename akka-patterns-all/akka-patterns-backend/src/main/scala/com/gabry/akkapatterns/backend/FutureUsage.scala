package com.gabry.akkapatterns.backend

import scala.concurrent.Future

/**
  * Created by gabry on 2018/7/10 15:56
  */
object FutureUsage {
  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val futureResult = Future{
      println(s"Future's Current timestamp ${System.currentTimeMillis()},thread id ${Thread.currentThread().getId}")
      Thread.sleep(1*1000)
      println("Future say HelloWorld")
      "Hello World"
    }
    println(s"Main thread's Current timestamp ${System.currentTimeMillis()},thread id ${Thread.currentThread().getId}")
    println("you can do other thing when future exec backend")
    Thread.sleep(3*1000)
    println("Main thread get future's result")
    futureResult.foreach(println)
  }
}

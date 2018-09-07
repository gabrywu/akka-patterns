package com.gabry.akkapatterns.whiles

/**
  * Created by gabry on 2018/7/17 9:10
  */
object GeneralWhile {
  def test(opt:Option[Int]):Unit = {
    println("hello")
    println(" in try")
  }
  def main(args: Array[String]): Unit = {
    var i=0
    val maxLine = 10
    while(i<maxLine){
      println(i)
      i += 1
    }
  }
}

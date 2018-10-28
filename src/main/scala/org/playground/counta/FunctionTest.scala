package org.playground.counta

object FunctionTest {
  def main(args: Array[String]): Unit = {
    def trans1 = {
      a:String=> a+"_1"
    }

    def trans2 ={
      a:String => a+"_2"
    }

    val a = "0"
    println(trans2(trans1(a)))
  }
}

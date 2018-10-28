package org.playground.counta

class DefaultSource (a:Int,b:Int){
//  def this() = {
//    this(3,4)
//  }

  def this(){this(3,4)}

  def hello()={
    println("a = "+a+", b = "+b)
  }

}

object DefaultSource {
  def main(args: Array[String]) {

    val a = new DefaultSource()
    a.hello()

  }
}
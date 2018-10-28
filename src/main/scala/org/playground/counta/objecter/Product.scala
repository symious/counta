package org.playground.counta.objecter

class Product private(var state : Int) {
  def DoSomething() = {
    state += 1
    System.out.println("I did something for the " + this.state + " time")
  }
}

object Product {
  private val _instance = new Product(0)
  def instance() =
    _instance

  def main(args: Array[String]): Unit = {
    import scala.collection.JavaConverters._
    val m = System.getProperties.stringPropertyNames().asScala
      .map(key => (key, System.getProperty(key))).toMap

    println(m)

  }
}

class BaseException(message: String, cause: Throwable) extends Exception(message, cause){
  def this(message: String) = this(message, null)
}

class FirstException(cause: Throwable) extends BaseException("I'm first Exception")
class SecondException(cause: Throwable) extends BaseException("I'm second Exception")



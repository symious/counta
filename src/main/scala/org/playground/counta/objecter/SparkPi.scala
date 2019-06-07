package org.playground.counta.objecter

import org.apache.spark.sql.SparkSession

/** Computes an approximation to pi */
object SparkPi {
  def main(args: Array[String]) {
    val r = scala.util.Random
    val spark = SparkSession
      .builder
      .appName("Spark Pi")
      .getOrCreate()
    val slices = if (args.length > 0) args(0).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
    val res = spark.sparkContext.parallelize(1 until n, slices).map { i =>
      (r.nextInt(1000),SparkFunUtil.getLocalHost())
    }.reduce((a,b)=> if(a._1>b._1) a else b)
    println(s"winner is ${res._2} and result is ${res._1}!")
    spark.stop()
  }
}


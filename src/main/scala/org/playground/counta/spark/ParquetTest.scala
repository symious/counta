package org.playground.counta.spark

import org.apache.spark.sql.SparkSession

object ParquetTest extends App{

  val spark = SparkSession
      .builder()
      .appName("Java Spark SQL basic example")
      .config("spark.master","local")
      .getOrCreate()

  spark.read.parquet("hdfs://10.128.151.161:8020/products/shopee/profile/item_profile/benchmark")

}

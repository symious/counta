package org.playground.counta

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import org.playground.counta.util._

object DSchema {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .config("spark.hadoop.mapred.output.compress", "true")
      .config("spark.hadoop.mapred.output.compression.codec", "org.apache.hadoop.io.compress.GzipCodec")
      .config("spark.hadoop.mapred.output.compression.type", "BLOCK")
      .getOrCreate()


    import DataFrameUtils._
    import spark.implicits._
    val HDFS_FS = FileSystem.get(spark.sparkContext.hadoopConfiguration)


    for (r <- List("ID")) {
      for (i <- 0 to 23) {
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=$i"
        HDFS_FS.create(new Path(tmp_dir), true);
      }

      for (i <- 0 to 22) {
        println(s"dealing with $r : $i")
        val df = spark.read.parquet(s"/products/shopee/inhouse/raw_v3/grass_region=$r/grass_date=2018-05-10/hour=$i")
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=$i"

        val df2 = df.withNestedColumn("viewed_object.item.discount_percentage", $"data.item.discount_percentage")
        df2.write.mode("overwrite").partitionBy("operation").parquet(tmp_dir)
      }
    }

    for (r <- List("PH", "TH", "VN")) {
      for (i <- 0 to 23) {
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=$i"
        HDFS_FS.create(new Path(tmp_dir), true);
      }

      for (i <- 0 to 22) {
        println(s"dealing with $r : $i")
        val df = spark.read.parquet(s"/products/shopee/inhouse/raw_v3/grass_region=$r/grass_date=2018-05-10/hour=$i")
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=$i"

        val df2 = df.withNestedColumn("viewed_object.item.discount_percentage", $"data.item.discount_percentage")
        df2.write.mode("overwrite").partitionBy("operation").parquet(tmp_dir)
      }
    }
    for (r <- List("TW")) {
      for (i <- 0 to 23) {
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=$i"
        HDFS_FS.create(new Path(tmp_dir), true);
      }

      for (i <- 0 to 22) {
        println(s"dealing with $r : $i")
        val df = spark.read.parquet(s"/products/shopee/inhouse/raw_v3/grass_region=$r/grass_date=2018-05-10/hour=$i")
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=$i"

        val df2 = df.withNestedColumn("viewed_object.item.discount_percentage", $"data.item.discount_percentage")
        df2.write.mode("overwrite").partitionBy("operation").parquet(tmp_dir)
      }
    }

    for (r <- List("MY", "ID", "TW", "PH", "TH", "VN")) {
      for (o <- List("action_add_to_cart_success", "action_delete_shopping_cart_item", "action_search_in_search_bar", "click", "impression", "view")) {
        val src_dir = s"/products/shopee/inhouse/raw_v3/grass_region=$r/grass_date=2018-05-10/hour=23/operation=$o"
        println(s"dealing with $src_dir")
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=23/operation=$o"
        val status = HDFS_FS.listStatus(new Path(src_dir))
        status.foreach(x => {
          val df = spark.read.parquet(x.getPath.toString)
          val df2 = df.withNestedColumn("viewed_object.item.discount_percentage", $"data.item.discount_percentage")
          df2.write.mode("append").parquet(tmp_dir)
        }
        )
      }
    }

    for (r <- List("MY", "ID", "TW", "PH", "TH", "VN")) {
      for (o <- List("action_add_to_cart_success", "action_delete_shopping_cart_item", "action_search_in_search_bar", "click", "impression", "view")) {
        val src_dir = s"/products/shopee/inhouse/raw_v3/grass_region=$r/grass_date=2018-05-10/hour=23/operation=$o"
        println(s"dealing with $src_dir")
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=23/operation=$o"
        HDFS_FS.create(new Path(tmp_dir), true);
        val status = HDFS_FS.listStatus(new Path(src_dir))
        status.foreach(x => {
          val df = spark.read.parquet(x.getPath.toString)
          val df2 = df.withNestedColumn("viewed_object.item.discount_percentage", $"data.item.discount_percentage")
          df2.write.mode("append").parquet(tmp_dir)
        }
        )
      }
    }
    for (r <- List("SG", "MY", "ID", "TW", "PH", "TH", "VN")) {
      for (o <- List("action_add_to_cart_success", "action_delete_shopping_cart_item", "action_search_in_search_bar", "click", "impression", "view")) {
        val src_dir = s"/products/shopee/inhouse/raw_v3/grass_region=$r/grass_date=2018-05-10/hour=23/operation=$o"
        println(s"dealing with $src_dir")
        val tmp_dir = s"/user/yiyang.zhou/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10/hour=23/operation=$o"
        HDFS_FS.create(new Path(tmp_dir), true)
      }
    }

    for (r <- List("SG", "MY", "ID", "TW", "PH", "TH", "VN")) {
        println(s"dealing with $r ")
        val df = spark.read.parquet(s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp/grass_region=$r/grass_date=2018-05-10")
        val tmp_dir = s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_res/grass_region=$r/grass_date=2018-05-10"

        val df2 = df.withNestedColumn("data.item.location",$"viewed_object.item.location")
        df2.write.mode("overwrite").partitionBy("hour","operation").parquet(tmp_dir)
    }

    val df = spark.read.parquet(s"/user/yiyang.zhou/products/shopee/inhouse/raw_v3_tmp")
    val tmp_dir = s"/products/shopee/inhouse/raw_v3_help"

    val df2 = df.dropNestedColumn("viewed_object.item.location")
    df2.write.mode("overwrite").partitionBy("grass_region","grass_date","hour","operation").parquet(tmp_dir)
  }
}

package org.playground.counta

object UploadTest {

  def main(args: Array[String]): Unit = {

    val map = Map("srcPath"->"srcPathValue","tableName"->"tableNameRalue")
    println(map)
    println(map.get("srcPath"))
    println(map.get("srcPath").get)
    println(map.get("tableName"))
    println(map.get("tableName").get)
    println(map.get("tableName1"))
    println(map.get("tableName1").get)
  }
}

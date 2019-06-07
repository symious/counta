package org.playground.counta.jmx


import javax.management.{MBeanAttributeInfo, MBeanServerConnection, ObjectName}
import javax.management.remote.{JMXConnector, JMXConnectorFactory, JMXServiceURL}
import scala.util.control.Breaks._


object HiveMetastoreJMXTest {

  //test cluster in telin
  //val jmxURL = "service:jmx:rmi:///jndi/rmi://10.128.151.161:9093/jmxrmi"

  //scluster
  val jmxURL = "service:jmx:rmi:///jndi/rmi://10.65.205.34:9093/jmxrmi"
  val serviceURL = new JMXServiceURL(jmxURL)
  val connector:  JMXConnector = JMXConnectorFactory.connect(serviceURL)
  val mbsc: MBeanServerConnection = connector.getMBeanServerConnection()

  import scala.collection.JavaConversions._
  val objectNames: java.util.Set[ObjectName] = mbsc.queryNames(null, null)

  // Print name, mbean name, attribute
  for (objectName <- objectNames) {
    println("objectName: " + objectName.getCanonicalName)
    val attributes: Array[MBeanAttributeInfo] = mbsc.getMBeanInfo(objectName).getAttributes()
    for (attribute:MBeanAttributeInfo  <- attributes){
      try {
        val attribute1: Object = mbsc.getAttribute(objectName, attribute.getName())
        println("\t" + attribute.getName +" = " + attribute1)
      } catch {
        case e: Exception =>
      }
    }
  }

  breakable {
    for (objectName <- objectNames) {
      val prefix = getPrefix(objectName.getCanonicalName())
      if (prefix == null) {
        break
      }
      val attributes: Array[MBeanAttributeInfo] = mbsc.getMBeanInfo(objectName).getAttributes()
      for (attribute:MBeanAttributeInfo  <- attributes){
        try {
          val attribute1: Object = mbsc.getAttribute(objectName, attribute.getName())
          val value: Double = attribute1.toString().toDouble
          val metricName: String = prefix + "_" + attribute.getName()
          System.out.println(metricName + "=" + value)
        } catch {
          case e: Exception =>
        }
      }
    }
  }

  breakable {
    for (objectName <- objectNames) {
      val prefix = getPrefix(objectName.getCanonicalName())
      if (prefix == null) {
        break
      }
      val attributes: Array[MBeanAttributeInfo] = mbsc.getMBeanInfo(objectName).getAttributes()
      for (attribute:MBeanAttributeInfo  <- attributes){
        try {
          val attribute1: Object = mbsc.getAttribute(objectName, attribute.getName())
          val metricName: String = prefix + "_" + attribute.getName()
          System.out.println(metricName+": "+attribute1.toString)
        } catch {
          case e: Exception =>
        }
      }
    }
  }

  def getPrefix(nameString: String): String= {
    try {
      val nameSplit = nameString.split(",")

      var mtype: String = null
      var mname: String = null
      var mcomponent: String = null

      for (namePart <- nameSplit) {
        val splitKV = namePart.split("=")
        val key = splitKV(0)
        if (key.endsWith("type")) {
          mtype = if (splitKV.length < 2) null else splitKV(1)
        } else if (key.endsWith("name")) {
          val nameBuilder: StringBuilder = new StringBuilder()
          for (i <- 1 to splitKV.length) {
            if (i != 1) {
              nameBuilder.append("_")
            }
            if (i == 2) {
              nameBuilder.append(splitKV(i).replace(" ", "_"))
            } else {
              nameBuilder.append(splitKV(i))
            }
          }
          mname = nameBuilder.toString()
        } else if (key.endsWith("component")) {
          mcomponent = if (splitKV.length < 2)null else splitKV(1)
        }
      }
//      //如果type为数字 则不进行处理
//      if (StringUtils.isNumeric(mtype)) {
//        return null
//      }

      //按照type name component 顺序拼接
      val sb = new StringBuilder()
      sb.append(if(mtype == null) "" else mtype)
      if (mname != null && mtype != null) {
        sb.append("_")
      }
      //去掉空格，点. 冒号: 引号" 转换为下划线_
      sb.append(if(mname == null) "" else mname.replace(" ", "")
        .replace('.', '_').replace(":", "_")
        .replace("\"", ""))
      if (mcomponent != null && mname != null) {
        sb.append("_")
      }
      sb.append(if(mcomponent == null) "" else mcomponent)
      sb.toString()
    } catch{
      case e :Exception => null
    }
  }

}

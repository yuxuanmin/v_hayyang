package com.hayyang.bigdata.rdd.action

import breeze.signal.OptWindowFunction.User
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_foreach {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3), ("c", 4)))
    val rdd2 = sc.makeRDD(List(1,2,3,4),2)
    val rdd3 = sc.makeRDD(List(1,2,3,4))

//    version(List("3.11.814.100","3.11.1277.81"))



    val user = new User1
    rdd3.foreach{
      number => (println("age = " + (user.age + number)))
    }


    def version(i:List[String]): Unit = {
      for (a <- i){
        val strings: Array[String] = a.split("\\.", 0)
        println(a + "\t" + (strings(0).toInt * scala.math.pow(65536, 3)
          + strings(1).toInt * scala.math.pow(65536, 2)
          + strings(2).toInt * scala.math.pow(65536, 1)
          + strings(3).toInt * scala.math.pow(65536, 0))
        )
      }
    }




    sc.stop()
  }
}
// 样例类在编译时，会自动混入序列化特质（实现可序列化接口）
case class User1(){
  var age :Int = 30
}

class User extends  Serializable{
  var age : Int = 30
}
package com.hayyang.bigdata.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_01_cogroup {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3)))
    val rdd2 = sc.makeRDD(List(("b", 4), ("a", 5), ("f", 6)))


    rdd2.foreach(println)
    // 分组连接

    val cogRDD:RDD[(String,(Iterable[Int],Iterable[Int]))] = rdd1.cogroup(rdd2)
    cogRDD.collect.foreach(println)

//    rdd1.cogroup()

    sc.stop()
  }
}

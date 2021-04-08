package com.hayyang.bigdata.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_join {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3)))
    val rdd2 = sc.makeRDD(List(("b", 4), ("a", 5), ("f", 6)))


    rdd2.foreach(println)
    // 两个不同的数据源的数据，相同的key的value会连在一起，形成元祖
    // 如果两个数据源中key没有相同的key那么数据不会出现在结果中
    // 如果两个数据源中key有多个相同的，回一次匹配，可能出现笛卡尔乘积，数据量会几何增长

    // 内连接
    println("------------------ join -------------------------")
    val joinRDD: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    joinRDD.collect.foreach(println)

    // 左连接
    println("------------------ leftJoinRDD -------------------------")
    val leftJoinRDD = rdd1.leftOuterJoin(rdd2)
    leftJoinRDD.collect.foreach(println)

    // 右连接
    println("------------------ rightJoinRDD -------------------------")
    val rightJoinRDD = rdd1.rightOuterJoin(rdd2)
    rightJoinRDD.collect.foreach(println)
//    rightJoinRDD.collect.foreach(println)
    sc.stop()
  }
}

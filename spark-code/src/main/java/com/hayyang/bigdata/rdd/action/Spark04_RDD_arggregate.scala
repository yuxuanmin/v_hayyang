package com.hayyang.bigdata.rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_arggregate {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(1,2,3,4),2)


    // aggregateByKey : 初始值只会参与分区内计算
    // aggregate : 初始值不仅参与分区内计算，也参加分区间计算
    val i: Int = rdd1.aggregate(10)(_ + _, _ + _)
    println(i)


    val i1: Int = rdd1.fold(10)(_ + _)
    println(i1)

    sc.stop()
  }
}

package com.hayyang.bigdata.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Memory_Par {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")
    val rdd = sc.makeRDD(List(1, 2, 3, 4)).repartition(1)
    rdd.saveAsTextFile("output2")
    sc.stop()
  }
}

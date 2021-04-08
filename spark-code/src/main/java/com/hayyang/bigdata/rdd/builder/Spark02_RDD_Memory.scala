package com.hayyang.bigdata.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Memory {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)

  
    sc.stop()
  }
}

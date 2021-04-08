package com.hayyang.bigdata.rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_countByKey {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3), ("c", 4)))
    val rdd2 = sc.makeRDD(List(1,2,3,4),2)




//    val tupleToLong: collection.Map[Int, Long] = rdd2.countByValue()
//    println(tupleToLong)

       val tupleToLong2: collection.Map[String, Long] = rdd1.countByKey()
       println(tupleToLong2)

    sc.stop()
  }
}

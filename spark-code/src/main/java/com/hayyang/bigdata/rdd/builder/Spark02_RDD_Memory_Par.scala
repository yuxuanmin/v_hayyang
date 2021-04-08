package com.hayyang.bigdata.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Memory_Par {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd = sc.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)))
    val groupRDD = rdd.groupByKey()
    //groupRDD.map()
    groupRDD.map(x => (x._1,x._2.sum)).collect().foreach(println)
    groupRDD.foreach(println)

    rdd.reduceByKey(_+_).collect().foreach(println)

    sc.stop()
  }
}

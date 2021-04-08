package com.hayyang.bigdata.rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Cache {
  def main(args: Array[String]): Unit = {




    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    //    val session: SparkSession = new SparkSession()
    sc.setLogLevel("error")
    val lines = sc.textFile("datas/2.txt")

    val words: RDD[String] = lines.flatMap(_.split(" "))



    val mapWord: RDD[(String, Int)] = words.map(words => {
      println("$"*20)
      (words,1)
    })


    // cache 默认持久化的操作缓存在内存中，如果需要设置其他的持久化储存方式就需要用 persist
    // 如果一个RDD需要重复使用,那么需要从头再次执行来获取数据RDD对象可以重用的,但是数据无法重用,因此需要在行动算子之前将数据做一个持久化操作
    // RDD对象的持久化操作不一定是为了重用,如果前面运行的路径太长也是可以进行持久化操作
    mapWord.cache()
    mapWord.persist(StorageLevel.DISK_ONLY)

    val wordCount = mapWord.reduceByKey(_ + _)
    val groupRDD = mapWord.groupByKey()

    groupRDD.foreach(println)
    wordCount.foreach(println)

  }
}

package com.hayyang.bigdata.rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Checkpoint {
  def main(args: Array[String]): Unit = {




    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    //    val session: SparkSession = new SparkSession()
    sc.setLogLevel("error")
    sc.setCheckpointDir("cp")
    val lines = sc.textFile("datas/2.txt")

    val words: RDD[String] = lines.flatMap(_.split(" "))



    val mapWord: RDD[(String, Int)] = words.map(words => {
      println("$"*20)
      (words,1)
    })


    // checkpoint 需要落盘，需要指定检查点保存路径
    // 检查点路径保存的文件,当作业执行完毕后,不会被删除
    // 一般保存路径都是分布式储存系统:HDFS
    /*
    * cache:经数据临时存储在内存中进行数据重用
    * persist：经数据临时存储再送磁盘文件中进行数据重用
    *         涉及到磁盘IO，性能较低
    *
    * */
//    mapWord.cache()
//    mapWord.persist(StorageLevel.DISK_ONLY)
    mapWord.checkpoint()

    val wordCount = mapWord.reduceByKey(_ + _)
    val groupRDD = mapWord.groupByKey()

    groupRDD.collect().foreach(println)
    wordCount.collect().foreach(println)

//    groupRDD.foreach(println)
//    wordCount.foreach(println)

  }
}

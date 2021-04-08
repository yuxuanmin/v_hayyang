package com.hayyang.bigdata.rdd.dep

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Dep {
  def main(args: Array[String]): Unit = {
    // 血缘关系
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    //    val session: SparkSession = new SparkSession()
    sc.setLogLevel("error")
    val lines = sc.textFile("datas/2.txt")
    lines.foreach(println)
    println(lines.toDebugString)
    println("*"*30)

    val words: RDD[String] = lines.flatMap(_.split(" "))
    println(words.toDebugString)
    println("*"*30)


    val mapWord: RDD[(String, Int)] = words.map(x => (x, 1))
    println(mapWord.toDebugString)
    println("*"*30)


    val wordCount = mapWord.reduceByKey(_ + _)
    println(wordCount.toDebugString)
    println("*"*30)


    wordCount.foreach(println)

  }
}

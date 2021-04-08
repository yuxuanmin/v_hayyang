package com.hayyang.bigdata.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("wordCount")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")
    val lines = sc.textFile("datas")
    val words = lines.flatMap(_.split(" "))
    //    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy(x => x)
    //    val wordToCount: RDD[(String, Int)] = wordGroup.map {
    //      case (word, list) => {
    //        (word, list.size)
    //      }
    //    }
    //    val array = wordToCount.collect()
    //    array.foreach(println)
    val wordToOne: RDD[(String, Int)] = words.map(x => (x, 1))
    val wordToCount = wordToOne.reduceByKey(_+_)

    val array = wordToCount.collect()
    array.foreach(println)
    sc.stop()

  }

}



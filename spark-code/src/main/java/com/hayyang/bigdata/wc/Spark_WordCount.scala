package com.hayyang.bigdata.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object Spark_WordCount {

  def main(args: Array[String]): Unit = {
//    val session = SparkSession.builder().appName("localname").master("local[*]").getOrCreate()
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCount")
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
    val wordGroup: RDD[(String, Iterable[(String, Int)])] = wordToOne.groupBy(x => x._1)
    wordGroup.foreach(println)
    val wordToCount: RDD[(String, Int)] = wordGroup.map {
      case (word, list) => {
        list.reduce(
          (t1, t2) => {
//            println("----------------------")
//            println(t1, t2)
            (t1._1, t1._2 + t2._2)
//            (t1._1,t1._2,t2._1,t2._2)
          }
        )
      }
    }
    val array = wordToCount.collect()
    array.foreach(println)
    sc.stop()

  }

}



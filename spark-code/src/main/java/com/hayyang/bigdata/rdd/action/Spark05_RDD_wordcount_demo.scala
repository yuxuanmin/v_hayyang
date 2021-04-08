package com.hayyang.bigdata.rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark05_RDD_wordcount_demo {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3), ("c", 4)))
    val rdd2 = sc.makeRDD(List("Hello Scala","Hello Spark"))
    val words: RDD[String] = rdd2.flatMap(_.split(" "))

    //1、groupBy
    println("*"*30 + "1.groupBy" + "*" * 30)
    words.groupBy(x => x).mapValues(x => x.size).foreach(println)


    //2、groupByKey
    println("*"*30 + "2.groupByKey" + "*" * 30)
    words.map(x => (x,1)).groupByKey().mapValues(x => x.size).foreach(println)


    //3、reduceByKey
    println("*"*30 + "3.reduceByKey" + "*" * 30)
    words.map(x => (x,1)).reduceByKey(_+_).foreach(println)


    //4、aggregateByKey
    println("*"*30 + "4.aggregateByKey" + "*" * 30)
    words.map(x => (x,1)).aggregateByKey(0)(_+_,_+_).foreach(println)

    //5、foldByKey
    println("*"*30 + "5.foldByKey" + "*" * 30)
    words.map(x => (x,1)).foldByKey(0)(_+_).foreach(println)

    //6、combineByKey
    println("*"*30 + "6.combineByKey" + "*" * 30)
    words.map(x => (x,1)).combineByKey(
      v =>v,
      (x:Int,y) => x + y,
      (x:Int,y:Int) => x + y
    ).foreach(println)

    //7、countByKey
    println("*"*30 + "7.countByKey" + "*" * 30)
    val stringToLong: collection.Map[String, Long] = words.map(x => (x,1)).countByKey()
    stringToLong.foreach(println)

    //8.countByValue
    println("*"*30 + "8.countByValue" + "*" * 30)
    val stringToLong1: collection.Map[String, Long] = words.countByValue()
    stringToLong1.foreach(println)

    //reduce
    println("*"*30 + "9.reduce" + "*" * 30)
    val mapWord: RDD[mutable.Map[String, Long]] = words.map(
      word => {
        mutable.Map[String, Long]((word, 1))
      }
    )
    val stringToInt: mutable.Map[String, Long] = mapWord.reduce(
      (map1, map2) => {
        map2.foreach {
          case (word, count) => {
            val newCount = map1.getOrElse(word, 0L) + count
            map1.update(word, newCount)
          }
        }
        map1
      }
    )
    stringToInt.foreach(println)
    sc.stop()
  }
}

package com.hayyang.bigdata.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Req {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

    //

    // 1.获取原始数据

    val lines = sc.textFile("datas/agent.log")
    val mapRDD = lines.map {
      lines => {
        val line: Array[String] = lines.split(" ")
        ((line(1), line(4)), 1)
      }
    }
    val newMapRDD: RDD[(String, Iterable[(String, Int)])] = mapRDD.reduceByKey(_ + _).map(x => (x._1._1, (x._1._2, x._2))).groupByKey()

    newMapRDD.mapValues{
      x =>{
        x.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
//        x.toList.sortBy()
      }
    }.collect.foreach(println)
    println("*" * 50)
    val value: RDD[(String, List[(String, Int)])] = newMapRDD.mapValues {
      y => {
        y.toList.sortWith(_._2 > _._2).take(3)
      }
    }
    value.collect.foreach(println)



    sc.stop()
  }
}

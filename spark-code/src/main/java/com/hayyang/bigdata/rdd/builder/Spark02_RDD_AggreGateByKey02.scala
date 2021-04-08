package com.hayyang.bigdata.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_AggreGateByKey02 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd = sc.makeRDD(List(("a", 1), ("a", 2), ("b", 3), ("a", 4), ("a", 5), ("b", 6)),2)
    val value: RDD[(String, List[Any])] = rdd.mapPartitionsWithIndex {
      (partid, iter) => {
        var part_map = scala.collection.mutable.Map[String, List[Any]]()
        var part_name = "part_" + partid
        part_map(part_name) = List[Any]()
        while (iter.hasNext) {
          part_map(part_name):+=iter.next()
        }
        part_map.iterator
      }
    }
    value.foreach(println)

    val newRDD: RDD[(String, (Int, Int))] = rdd.aggregateByKey((0, 0))(
      (k, v) => {
        println("*" * 30)
        println(k,v)
        println("-" * 30)
        (k._1 + v, k._2 + 1)
      },
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )
    newRDD.foreach(x => println("newRDD" + x))
    val value1: RDD[(String, Int)] = newRDD.map(x => (x._1, x._2._1 / x._2._2))
    val value2: RDD[(String, Int)] = newRDD.mapValues(x => x._1 / x._2)
    value1.collect().foreach(println)
    value2.collect().foreach(println)
    sc.stop()
  }
}

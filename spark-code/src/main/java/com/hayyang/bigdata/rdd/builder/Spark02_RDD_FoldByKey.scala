package com.hayyang.bigdata.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_FoldByKey {
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


    rdd.aggregateByKey(0)(
      _+_,_+_
    ).collect.foreach(println)


    // 如果分区内和分区间计算逻辑一样，spark提供了简化的方法

    rdd.foldByKey(0)(_+_).collect.foreach(println)

//    rdd.fold()
    sc.stop()
  }
}

package com.hayyang.bigdata.seq

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Req3_HotCategoryTop10 {
    def main(args: Array[String]): Unit = {
//
//
//        // 前面reduceByKey 太多影响性能
//        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
//        val sc = new SparkContext(sparkConf)
//        sc.setLogLevel("error")
//
//        val actionRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")
//        val actionRDD1: RDD[String] = sc.textFile("datas/1.txt")
//
//        val spRDD: RDD[Array[String]] = actionRDD.map((_.split("\t")))
//
//        spRDD.cache()
//
//        // 统计商品点击数量
//        // 统计商品下单数量
//        // 统计商品的支付人数
//        val value1: RDD[(String, (Int, Int, Int))] = spRDD.flatMap(
//            line => {
//                if (line(6) != "-1") {
//                    List((line(6), (1, 0, 0)))
//                } else if (line(8) != "null") {
//                    line(8).split(",").map(x => (x, (0, 1, 0)))
//                } else if (line(10) != "null") {
//                    line(10).split(",").map(((_), (0, 0, 1)))
//                } else {
//                    Nil
//                }
//
//            }
//        )
//
//
//
//
////        val allRDD: RDD[(String, (Int, Int, Int))] = clickRDD.union(orderRDD).union(payRDD)
//
//        val value: RDD[(String, (Int, Int, Int))] = value1.reduceByKey(
//            (t1, t2) => {
//                (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
//            }
//        )
//
//        val tuples: Array[(String, (Int, Int, Int))] = value.sortBy(_._2,false).take(10)
//
//        tuples.foreach(println)
//
//        sc.stop()

    }
}

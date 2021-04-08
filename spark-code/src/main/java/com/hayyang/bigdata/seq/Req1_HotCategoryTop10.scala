package com.hayyang.bigdata.seq

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Req1_HotCategoryTop10 {
    def main(args: Array[String]): Unit = {

        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("error")

        val actionRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")
        val actionRDD1: RDD[String] = sc.textFile("datas/1.txt")
//        actionRDD.collect().foreach(println)
        val spRDD: RDD[Array[String]] = actionRDD.map((_.split("\t")))

//        spRDD.filter(x => x(6) != "-1").collect().foreach(println)
//        spRDD.map(x => x(6)).collect().foreach(println)

//        actionRDD.map(
//            line => {
//                val strings = line.split("\t")
//                strings
//            }
//        ).map(x => x(6)).collect().foreach(println)
        spRDD.cache()
        // 统计商品点击数量
        val clickRDD: RDD[(String, Int)] = spRDD.filter(
            line => {
                line(6) != "-1"
            }
        ).map((x => (x(6), 1))).reduceByKey(_ + _)


        // 统计商品下单数量
        val orderRDD: RDD[(String, Int)] = spRDD.filter(x => x(8) != "null").flatMap(
            line => {
                val strings: Array[String] = line(8).split(",")
                strings.map((_, 1))
            }
        ).reduceByKey(_ + _)

        // 统计商品的支付人数
        val payRDD: RDD[(String, Int)] = spRDD.filter(x => x(10) != "").flatMap(
            line => {
                val strings = line(10).split(",")
                strings.map((_, 1))
            }
        ).reduceByKey(_ + _)

        val coRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickRDD.cogroup(orderRDD, payRDD)

        val analysisRDD: RDD[(String, (Int, Int, Int))] = coRDD.mapValues {

            case (clickIter, orderIter, payIter) => {
                var clickCnt = 0
                var orderCnt = 0
                var payCnt = 0

                val iter1 = clickIter.iterator
                if (iter1.hasNext) {
                    clickCnt = iter1.next()
                }

                val iter2 = orderIter.iterator
                if (iter2.hasNext) {
                    orderCnt = iter2.next()
                }

                val iter3 = payIter.iterator
                if (iter3.hasNext) {
                    payCnt = iter3.next()
                }
                (clickCnt, orderCnt, payCnt)
            }
        }

        val tuples = analysisRDD.sortBy(_._2, false).take(10)
        tuples.foreach(println)
        sc.stop()

    }
}

package com.hayyang.bigdata.seq

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.dmg.pmml.False

object Req2_HotCategoryTop10 {
    def main(args: Array[String]): Unit = {


        // cogroup 有几率产生shuflle 转换3个RDD 的结构 用union聚合
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("error")

        val actionRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")
        val actionRDD1: RDD[String] = sc.textFile("datas/1.txt")

        val spRDD: RDD[Array[String]] = actionRDD.map((_.split("\t")))

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

        val clickRDD1: RDD[(String, (Int, Int, Int))] = clickRDD.mapValues((_,0,0))

        val orderRDD1: RDD[(String, (Int, Int, Int))] = orderRDD.mapValues((0,_,0))

        val payRDD1: RDD[(String, (Int, Int, Int))] = payRDD.mapValues( (0, 0, _))


        val allRDD: RDD[(String, (Int, Int, Int))] = clickRDD1.union(orderRDD1).union(payRDD1)

        val value: RDD[(String, (Int, Int, Int))] = allRDD.reduceByKey(
            (t1, t2) => {
                (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
            }
        )

        val tuples: Array[(String, (Int, Int, Int))] = value.sortBy(_._2,false).take(10)

        tuples.foreach(println)

        sc.stop()

    }
}

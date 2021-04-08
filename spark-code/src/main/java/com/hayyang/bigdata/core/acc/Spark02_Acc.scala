package com.hayyang.bigdata.core.acc

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_Acc {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

    val value = sc.makeRDD(List(1, 2, 3, 4))

    val sumAcc = sc.longAccumulator("name")

    value.map(
      num => {
//        println(num)
        sumAcc.add(num)
      }
    )
    // map 少加 会出现少加的情况，转换算子调用累加器，古国没有行动算子，是不会执行的
    println(sumAcc.name,sumAcc.value,sumAcc.avg)
    sc.stop()
  }
}

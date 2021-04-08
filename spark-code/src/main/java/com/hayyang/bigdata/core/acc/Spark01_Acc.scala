package com.hayyang.bigdata.core.acc

import org.apache.spark.{SparkConf, SparkContext}

object Spark01_Acc {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

    val value = sc.makeRDD(List(1, 2, 3, 4))

    val sumAcc = sc.longAccumulator("name")

    value.foreach(
      num => {
        sumAcc.add(num)
      }
    )
    println(sumAcc.name,sumAcc.value,sumAcc.avg)
    sc.stop()
  }
}

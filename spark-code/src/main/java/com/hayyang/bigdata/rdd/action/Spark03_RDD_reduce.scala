package com.hayyang.bigdata.rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_reduce {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("error")

//    val rdd = sc.textFile("datas/1.txt",2)
//    rdd saveAsTextFile "output"
    val rdd1 = sc.makeRDD(List(1,2,3,4))

    //
    //

    val i:Int = rdd1.reduce(_ + _)
    println(i)


    // collect : 方法将不同分区的数据按照分区顺序采集到的Driver端内存中，形成数组
    val ints: Array[Int] = rdd1.collect()


    //count:数据源中的数据的个数
    val count: Long = rdd1.count()
    println(count)


    // first : 数据源中的数据的第一个
    val first: Int = rdd1.first()

    println(first)
    sc.stop()
  }
}

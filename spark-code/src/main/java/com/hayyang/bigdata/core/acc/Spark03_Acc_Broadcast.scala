 package com.hayyang.bigdata.core.acc

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

 object Spark03_Acc_Broadcast {

     def main(args: Array[String]): Unit = {
         val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
         val sc = new SparkContext(sparkConf)
         sc.setLogLevel("error")

         val map1: RDD[(String, Int)] = sc.makeRDD(List(
             ("a", 4)
             , ("b", 5)
             , ("c", 6)
         ))


         val map2: mutable.Map[String, Int] = mutable.Map(("a", 1), ("b", 2), ("c", 3))

         val bc: Broadcast[mutable.Map[String, Int]] = sc.broadcast(map2)

         map1.map{
             case (word,count) => {
                 // 访问广播变量
                 val i = bc.value.getOrElse(word, 0)
                 (word,(count,i))
             }
         }.collect().foreach(println)

         sc.stop()
     }
 }

package com.hayyang.bigdata.rdd.dep

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Dep {
  def main(args: Array[String]): Unit = {

    /*
     依赖关系
     org.apache.spark.OneToOneDependency (窄依赖）上游的RDD 被下游的一个RDD依赖
     org.apache.spark.ShuffleDependency (宽依赖）上游的RDD 被下游的多个RDD依赖
     Application -> Job -> Stage -> Task 每一层都是1对n的关系
     Application ： 初始化一个SparkContext 即生成一个Application
     Job : 一个action 算子就会生成一个Job
     Stage：Stage等于宽依赖（ShuffleDependency)的个数加1
     Task：一个Stage阶段中，最后一个RDD的分区个数就是Task的个数。
    * */



    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    //    val session: SparkSession = new SparkSession()
    sc.setLogLevel("error")
    val lines = sc.textFile("datas/2.txt")
    lines.foreach(println)
    println(lines.dependencies)
    println("*"*30)

    val words: RDD[String] = lines.flatMap(_.split(" "))
    println(words.dependencies)
    println("*"*30)


    val mapWord: RDD[(String, Int)] = words.map(x => (x, 1))
    println(mapWord.dependencies)
    println("*"*30)


    val wordCount = mapWord.reduceByKey(_ + _)
    println(wordCount.dependencies)
    println("*"*30)


    wordCount.foreach(println)

  }
}

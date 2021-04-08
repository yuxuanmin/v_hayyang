 package com.hayyang.bigdata.core.acc

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark03_Acc {

    def main(args: Array[String]): Unit = {
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("error")

        val value = sc.makeRDD(List("Hello","Spark","Hello"))

        val wcACC = new MyAccumulator()

        sc.register(wcACC,"wc")

//        val sumAcc = sc.longAccumulator("name")

        value.foreach(
            word =>{
                wcACC.add(word)
            }
        )

    // map 少加 会出现少加的情况，转换算子调用累加器，古国没有行动算子，是不会执行的
    println(wcACC.name,wcACC.value)
    sc.stop()
}
    class MyAccumulator extends AccumulatorV2[String,mutable.Map[String,Long]]{

        private val wcMap = mutable.Map[String,Long]()

        // 判断是否为初始状态
        override def isZero: Boolean = {
            wcMap.isEmpty
        }

        override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
            new MyAccumulator()
        }

        // 重置累加器
        override def reset(): Unit = {
            wcMap.clear()
        }

        // 获取累加器需要计算的值
        override def add(word: String): Unit = {
            val i: Long = wcMap.getOrElse(word, 0L)+1
            wcMap.update(word,i)
        }

        // Drive 合并多个累加器
        override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
            val map1 = this.wcMap
            val map2 = other.value
            map2.foreach{
                case (word, count) => {
                    val newCount = map1.getOrElse(word, 0L) + count
                    map1.update(word, newCount)
                }
            }
        }

        // 累加器的value
        override def value: mutable.Map[String, Long] = {
            wcMap
        }
    }
}

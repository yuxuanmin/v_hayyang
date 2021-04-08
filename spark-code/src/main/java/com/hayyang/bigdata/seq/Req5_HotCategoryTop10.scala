package com.hayyang.bigdata.seq

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Req5_HotCategoryTop10 {
    def main(args: Array[String]): Unit = {


        // 前面reduceByKey 太多影响性能
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val sc: SparkContext = new SparkContext(sparkConf)
        sc.setLogLevel("error")

        val actionRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")

        val spRDD: RDD[Array[String]] = actionRDD.map((_.split("\t")))

        val strings: List[String] = top10Categora(sc,actionRDD).map(_.cid)
        strings.foreach(println)
println("*"*50)
        val filterActionRDD: RDD[Array[String]] = spRDD.filter(x => {
            if (x(6) != "-1") {
                strings.contains(x(6))
            } else {
                false
            }
        })
        filterActionRDD.map(x => ((x(6), x(2)), 1)).reduceByKey(_ + _).map(x => (x._1._1, (x._1._2, x._2))).groupByKey().mapValues(
            x =>{
                x.iterator.toList.sortBy(_._2)(Ordering.Int.reverse)
            }
        ).collect().foreach(println)


        sc.stop()

    }

//        val categories1: List[HotCategory] = top10Categora(actionRDD)



        def top10Categora(sc:SparkContext,actionRDD: RDD[String]) ={
            val acc: HotCategoryAccumulator = new HotCategoryAccumulator

            sc.register(acc)

            actionRDD.foreach(
                line => {
                    val strings: Array[String] = line.split("\t")
                    if (strings(6) != "-1") {
                        acc.add((strings(6), "click"))
                    } else if (strings(8) != "null") {
                        val strings1: Array[String] = strings(8).split(",")
                        strings1.foreach(x => acc.add((x, "order")))
                    } else if (strings(10) != "null") {
                        val strings1: Array[String] = strings(10).split(",")
                        strings1.foreach(x => acc.add((x, "pay")))
                    } else {
                        Nil
                    }
                }
            )

            val value: mutable.Map[String, HotCategory] = acc.value

            val categories: List[HotCategory] = value.map(_._2).toList.sortWith(
                (t1, t2) => {
                    if (t1.clickCnt > t2.clickCnt) {
                        true
                    } else if (t1.clickCnt == t2.clickCnt) {
                        if (t1.orderCnt > t2.orderCnt) {
                            true
                        } else if (t1.orderCnt > t2.orderCnt) {
                            if (t1.payCnt > t2.payCnt) {
                                true
                            } else {
                                false
                            }
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
            )
            categories.take(10)
        }








    case class HotCategory(cid:String,var clickCnt:Int,var orderCnt:Int,var payCnt:Int)
    class HotCategoryAccumulator extends AccumulatorV2[(String,String),mutable.Map[String,HotCategory]]{

        private val hcMap : mutable.Map[String, HotCategory] = mutable.Map[String, HotCategory]()

        override def isZero: Boolean = hcMap.isEmpty

        override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] = new HotCategoryAccumulator

        override def reset(): Unit = hcMap.clear()

        override def add(v: (String, String)): Unit = {
            val cid = v._1
            val hcType = v._2
            val category: HotCategory = hcMap.getOrElse(cid, HotCategory(cid,0, 0, 0))
            if (hcType == "click"){
                category.clickCnt += 1
            } else if(hcType == "order"){
                category.orderCnt += 1
            } else {
              category.payCnt += 1
            }
            hcMap.update(cid,category)
        }

        override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]): Unit = {
            val map1 = this.hcMap
            val map2 = other.value
            map2.foreach{
                case (cid,hc) => {
                    val category: HotCategory = map1.getOrElse(cid, HotCategory(cid, 0, 0, 0))
                    category.clickCnt += hc.clickCnt
                    category.orderCnt += hc.orderCnt
                    category.payCnt += hc.payCnt

                    map1.update(cid,category)
                }
            }
        }

        override def value: mutable.Map[String, HotCategory] = hcMap
    }

}

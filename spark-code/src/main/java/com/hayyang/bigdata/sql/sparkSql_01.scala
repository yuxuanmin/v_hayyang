package com.hayyang.bigdata.sql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object sparkSql_01 {

    def main(args: Array[String]): Unit = {
        // 前面reduceByKey 太多影响性能
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        spark.sparkContext.setLogLevel("error")

        val df = spark.read.csv("datas\\2.txt")


        val df1: DataFrame = df.withColumnRenamed("_c0", "ds")
//        df1.show()
        df1.createOrReplaceTempView("ds_table")

        val fun:(String,Int) => String = (ds:String,add_mon:Int) => {
            var mon = ds.substring(4, 6).toInt
            var year = ds.substring(0, 4).toInt
//            println((mon+add_mon),(mon+add_mon) / 12,(mon+add_mon) % 12)
            println("*"*20)
            year = year + (mon+add_mon) / 12
            mon =  (mon+add_mon) % 12

//            println((mon+add_mon),(mon+add_mon) / 12,(mon+add_mon) % 12)
            if(mon == 0){

                (year-1).toString + "12"
            }else if(mon > 9){
                year.toString + mon.toString
            }
            else {
                year.toString + "0"+mon.toString
            }
//            year.toString + mon.toString
        }

        println(23/12)

        spark.udf.register("dayToMon",fun)

        spark.sql("select ds,dayToMon(ds,20) as mon,dayToMon(ds,21) as mon1,dayToMon(ds,22) as mon2 from ds_table").show()
        spark.close()
    }
}

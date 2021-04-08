package com.hayyang.bigdata.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object sparkSql_02 {

    def main(args: Array[String]): Unit = {
        // 前面reduceByKey 太多影响性能
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        spark.sparkContext.setLogLevel("error")

        val df = spark.read.csv("datas\\2.txt")


        val df1: DataFrame = df.withColumnRenamed("_c0", "ds")
//        df1.show()
        df1.createOrReplaceTempView("ds_table")





        spark.sql("select ds,dayToMon(ds,20) as mon,dayToMon(ds,21) as mon1,dayToMon(ds,22) as mon2 from ds_table").show()
        spark.close()
    }


}

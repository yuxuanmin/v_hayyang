package com.hayyang.bigdata.test

import java.io.ObjectOutputStream
import java.net.Socket

import com.hayyang.bigdata.test1.test.Task


object Driver {
  def main(args: Array[String]): Unit = {
    val client = new Socket("localhost", 9999)
    val out = client.getOutputStream
    val objOut = new ObjectOutputStream(out)
    val task = new Task()

    objOut.writeObject(task)
    objOut.flush()
    objOut.close()
    client.close()
    println("客户端数据发送成功")
  }
}

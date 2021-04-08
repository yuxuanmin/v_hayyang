package com.hayyang.bigdata.test

import java.io.{InputStream, ObjectInputStream}
import java.net.{ServerSocket, Socket}

import com.hayyang.bigdata.test1.test.Task

object Executor {
  def main(args: Array[String]): Unit = {
    val server = new ServerSocket(9999)
    println("服务器启动，等待连接")
    val client: Socket = server.accept()
    val in: InputStream = client.getInputStream

    val objIn = new ObjectInputStream(in)
    val task: Task = objIn.readObject().asInstanceOf[Task]
//    val ints: List[Int] = task.compute()
//
//
//    println("接收到客户端发送的数据："+ints)
    objIn.close()
    client.close()
    server.close()
  }
}

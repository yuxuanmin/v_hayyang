package com.hayyang.bigdata.test1.test

import java.io.{InputStream, ObjectInputStream}
import java.net.{ServerSocket, Socket}

object Executor2 {
  def main(args: Array[String]): Unit = {
    val server = new ServerSocket(8888)
    println("服务器启动，等待连接")
    val client: Socket = server.accept()
    val in: InputStream = client.getInputStream

    val objIn = new ObjectInputStream(in)
    val task: SubTask = objIn.readObject().asInstanceOf[SubTask]
//    val ints: List[Int] = task.compute()
//
//
//    println("接收到客户端发送的数据："+ints)
    objIn.close()
    client.close()
    server.close()
  }
}

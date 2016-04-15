package com.juanjo.akka.encrypt

object ServerApp extends App {

  val server = new Server

  server.init(null)
  server.start()

}

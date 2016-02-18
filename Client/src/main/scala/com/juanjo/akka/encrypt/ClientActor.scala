package com.juanjo.akka.encrypt

import akka.actor._
import com.juanjo.akka.encrypt.common.domain.ShowTextMessage

object ClientActor {
  val RemoteClientName = "remote-client"
  val ServerPath = "/user/server"
  val DefaultName = "client-actor"

  def props(): Props = Props(new ClientActor())
}

class ClientActor() extends Actor with ActorLogging {

  override def receive: Receive ={
    case ShowTextMessage(msg) => println(msg)
    case _ => println ("Other")
  }
}

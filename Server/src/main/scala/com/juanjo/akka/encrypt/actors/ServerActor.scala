package com.juanjo.akka.encrypt.actors

import akka.actor._
import com.juanjo.akka.encrypt.common.domain.SecureMessage
import com.juanjo.akka.encrypt.config.ServerConfig
import org.apache.log4j.Logger

object ServerActor {
  def props(): Props = Props(new ServerActor())
}


class ServerActor() extends Actor with ServerConfig  {
  lazy val logger = Logger.getLogger(classOf[ServerActor])

  def receive: Receive = {
    case SecureMessage(p) => logger.info(p)
    case any => logger.error(s"Unknown message: --------->" + self.path)
  }
}

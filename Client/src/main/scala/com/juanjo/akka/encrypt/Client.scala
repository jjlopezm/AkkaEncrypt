package com.juanjo.akka.encrypt

import akka.actor.ActorSystem
import akka.contrib.pattern.ClusterClient
import com.juanjo.akka.encrypt.common.domain.SecureMessage
import com.juanjo.akka.encrypt.config.ClientConfig
import com.typesafe.config.ConfigValue
import org.apache.log4j.Logger

import scala.collection.JavaConversions._
import scala.language.postfixOps

object Client extends ClientConfig {
  override lazy val logger = Logger.getLogger(getClass)
  def apply() = new Client()

}

class Client(properties: java.util.Map[String, ConfigValue]) {

  def this() = this(Map.empty[String, ConfigValue])

  def initClient(): Unit = {
    lazy val logger = Client.logger
    val finalConfig = properties.foldLeft(Client.config) { case (previousConfig, keyValue) =>
      previousConfig.withValue(keyValue._1, keyValue._2)
    }

    val system = ActorSystem("SecureCluster", finalConfig)

    if (logger.isDebugEnabled) {
      system.logConfiguration()
    }

    val serverNode = s"${Client.config.getStringList(ClientConfig.ServerNode)(0)}/user/server"
    val serverActor = system.actorSelection(serverNode)

    //message to server
    serverActor ! SecureMessage("Is this message secure?")

  }

}
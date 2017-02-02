package com.juanjo.akka.encrypt

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.juanjo.akka.encrypt.actors.ServerActor
import com.juanjo.akka.encrypt.config.ServerConfig
import org.apache.commons.daemon.{Daemon, DaemonContext}
import org.apache.log4j.Logger

class Server extends Daemon with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[Server])

  var system = ActorSystem(clusterName, config)
  val cluster = Cluster(system)

  override def init(p1: DaemonContext): Unit = ()

  override def start(): Unit = {

    system.actorOf(Props(new ServerActor(cluster)),config.getString(ServerConfig.SERVER_ACTOR_NAME_KEY))
    logger.info("Server Started")

  }

  override def stop(): Unit = {
    system.shutdown()
    logger.info("Server Stopped")
  }

  override def destroy(): Unit = ()
}

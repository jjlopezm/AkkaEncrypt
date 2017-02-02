package com.juanjo.akka.encrypt.actors

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, MemberUp, UnreachableMember}
import com.juanjo.akka.encrypt.common.domain.SecureMessage
import com.juanjo.akka.encrypt.config.ServerConfig
import org.apache.log4j.Logger

object ServerActor {
  def props(cluster: Cluster): Props = Props(new ServerActor(cluster))

}


class ServerActor(cluster:Cluster) extends Actor with ServerConfig  {
  lazy val logger = Logger.getLogger(classOf[ServerActor])

  override def preStart(): Unit = {
    super.preStart()
    cluster.subscribe(self, classOf[MemberEvent])

  }

  def receive: Receive = {
    case SecureMessage(p) => logger.info(s"******Secure message from a client: $p")
    case MemberUp(member) => logger.info(s"******New Member is up: $member")
    case UnreachableMember(member) =>logger.info(s"*******Unreachable member: $member")
  }
}

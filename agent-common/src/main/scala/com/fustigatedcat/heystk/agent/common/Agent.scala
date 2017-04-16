package com.fustigatedcat.heystk.agent.common

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem}
import akka.routing.RoundRobinPool
import com.fustigatedcat.heystk.agent.common.engine.EngineClient
import com.fustigatedcat.heystk.agent.common.normalization.{NormalizerSystem, NormalizerActor}
import com.fustigatedcat.heystk.common.normalization.Log
import com.typesafe.config.{ConfigFactory, Config}
import org.slf4j.LoggerFactory
import scala.concurrent.duration._

trait Agent {

  val logger = LoggerFactory.getLogger(this.getClass)

  var agentKey = ""

  lazy val config : Config = ConfigFactory.load(agentKey)

  val normalizationSystem = ActorSystem("normalization-system")

  lazy val normalizers = normalizationSystem.actorOf(
    RoundRobinPool(nrOfInstances = config.getInt("agent.normalizer-count")).
      props(Props(classOf[NormalizerActor], config, parseSystem(), engineClient))
  )

  lazy val engineClient = {
    implicit val dispatcher = normalizationSystem.dispatcher
    val ec = normalizationSystem.actorOf(Props(classOf[EngineClient], config))
    normalizationSystem.scheduler.schedule(
      0 milliseconds,
      config.getInt("engine.max-delay") minutes,
      ec,
      'POST
    )
    ec
  }

  def init() : Boolean

  def run() : Boolean

  def main(args : Array[String]) : Unit = {
    agentKey = args(0)
    Thread.currentThread().setName(config.getString("agent.name"))
    logger.info("Starting agent")
    init()
    normalizers
    logger.info("Starting processing")
    run()
  }

  def normalize(log : Log) = {
    normalizers ! log
  }

  def parseSystem() = {
    NormalizerSystem.create(config)
  }

}

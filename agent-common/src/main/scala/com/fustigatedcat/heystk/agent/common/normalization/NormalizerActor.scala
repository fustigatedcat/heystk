package com.fustigatedcat.heystk.agent.common.normalization

import akka.actor.{ActorRef, Actor}
import com.fustigatedcat.heystk.common.normalization.Log
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

class NormalizerActor(config : Config, system : NormalizerSystem, engine : ActorRef) extends Actor {

  val logger = LoggerFactory.getLogger(this.getClass)

  override def receive = {
    case log : Log => {
      logger.debug("Handling message [{}]", log)
      val norm = system.process(log)
      logger.debug("Created normalization [{}]", norm)
      engine ! norm
    }
  }

}

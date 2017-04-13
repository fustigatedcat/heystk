package com.fustigatedcat.heystk

import com.fustigatedcat.heystk.agent.common.Agent
import com.fustigatedcat.heystk.agent.common.normalization.Log
import org.apache.commons.io.input.{Tailer, TailerListener}
import org.slf4j.LoggerFactory

class AgentTailerListener(agent : Agent) extends TailerListener {

  val logger = LoggerFactory.getLogger(this.getClass)

  override def init(tailer: Tailer): Unit = {}

  override def fileNotFound(): Unit = {
    // TODO: should we exit here?
    logger.error("File not found")
  }

  override def handle(s: String): Unit = {
    agent.normalize(Log(s))
  }

  override def handle(e: Exception): Unit = {
    logger.error("Error handling logs", e)
  }

  override def fileRotated(): Unit = {
    // TODO: not sure if need to handle this
    logger.warn("File rotated")
  }

}

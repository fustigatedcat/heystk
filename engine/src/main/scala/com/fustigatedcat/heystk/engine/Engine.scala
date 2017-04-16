package com.fustigatedcat.heystk.engine

import com.fustigatedcat.heystk.engine.queue.RabbitQueue
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Engine {

  val logger = LoggerFactory.getLogger(this.getClass)

  val config = ConfigFactory.load()

  def main(args : Array[String]) : Unit = {
    logger.debug("Starting to process normalizations")
    RabbitQueue.startConsuming()
  }

}

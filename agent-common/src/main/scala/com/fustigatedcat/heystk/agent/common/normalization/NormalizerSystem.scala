package com.fustigatedcat.heystk.agent.common.normalization

import java.time.LocalDateTime
import java.util.Date

import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object NormalizerSystem {

  def create(config : Config) : NormalizerSystem = {
    new NormalizerSystem(
      config.getConfigList("agent.normalizer-system.chains").asScala.map(chain =>
        NormalizerChain.create(chain)
      ).toList
    )
  }

}

class NormalizerSystem(chains : List[NormalizerChain]) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(log : Log) : Normalization = {
    logger.trace("Beginning normalization")
    val rtn = chains.foldLeft(Normalization(log, new Date().getTime))((norm, chain) =>
      chain.process(norm)
    ).copy(endProcessing = new Date().getTime)
    logger.trace("Completing normalization")
    rtn
  }

}

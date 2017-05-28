package com.fustigatedcat.heystk.agent.common.normalization

import java.util.Date

import com.fustigatedcat.heystk.common.normalization.{Log, Normalization}
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
    val start = new Date()
    val extractors = chains.foldLeft(Map[String, Extractor]())((n, chain) => n ++ chain.process(log))
    val norm = Normalization(log, start.getTime, start.getTime, extractors.map(_._2.process(log)))
    logger.trace("Completing normalization")
    norm.copy(endProcessing = new Date().getTime)
  }

}

package com.fustigatedcat.heystk.agent.common.normalization

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object NormalizerChain {

  def create(config : Config) : NormalizerChain = {
    new NormalizerChain(
      config.getString("name"),
      config.getConfigList("normalizers").asScala.map(config =>
        Normalizer.create(config)
      ).toList
    )
  }

}

class NormalizerChain(name : String, normalizers : List[Normalizer]) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(normalization : Normalization) : Normalization = {
    logger.trace("Starting chain [{}]", name)
    val rtn = normalizers.foldLeft(normalization)((n,c) => c.process(n))
    logger.trace("Completing chain [{}]", name)
    rtn
  }

}

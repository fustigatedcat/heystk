package com.fustigatedcat.heystk.engine.processor

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.fustigatedcat.heystk.engine.rule.Rule
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Processor {

  def create(config : Config) : Processor = {
    val procConfig = config.getConfig("engine.processor")
    new Processor(
      procConfig.getConfigList("rules").asScala.map(Rule.create).toList
    )
  }

}

class Processor(rules : List[Rule]) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(normalization : Normalization) : Unit = {
    if(rules.exists(_.process(normalization))) {
      logger.debug("Rule match found")
    } else {
      logger.debug("No rule match found")
    }
  }

}

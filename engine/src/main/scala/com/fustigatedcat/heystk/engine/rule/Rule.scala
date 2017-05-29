package com.fustigatedcat.heystk.engine.rule

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Rule {

  def create(config : Config) : Rule = {
    new Rule(
      config.getString("name"),
      config.getBoolean("enabled"),
      RuleCriterion.create(config.getConfig("criterion")),
      config.getConfigList("actions").asScala.toList.map(Action.create)
    )
  }

}

class Rule(name : String, enabled : Boolean, criterion : RuleCriterion, val actions : List[Action]) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(normalization : Normalization) : Boolean = {
    if(criterion.matches(normalization)) {
      logger.debug("Rule {} matched", name)
      true
    } else {
      false
    }
  }

}

package com.fustigatedcat.heystk.engine.rule

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

object Rule {

  def create(config : Config) : Rule = {
    new Rule(
      config.getString("name"),
      config.getBoolean("enabled"),
      RuleCriterion.create(config.getConfig("criterion"))
    )
  }

}

class Rule(name : String, enabled : Boolean, criterion : RuleCriterion) {

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

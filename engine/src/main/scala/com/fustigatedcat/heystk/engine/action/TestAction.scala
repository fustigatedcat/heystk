package com.fustigatedcat.heystk.engine.action

import com.fustigatedcat.heystk.common.action.Action
import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config

class TestAction(config : Config) extends Action {

  val logNormalization = config.getBoolean("log-normalization")

  def execute(normalization : Normalization) = if(logNormalization) {
    logger.debug("Dropping normalization {}", normalization)
  } else {
    logger.debug("Dropping normalization UNLOGGED")
  }

}

package com.fustigatedcat.heystk.common.action

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

trait Action {

  val logger = LoggerFactory.getLogger(this.getClass)

  def execute(normalization : Normalization, runtimeConfig : Config) : Unit

}

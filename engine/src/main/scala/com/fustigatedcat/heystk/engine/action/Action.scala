package com.fustigatedcat.heystk.engine.action

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

object Action {

  def create(config : Config) : Action = {
    val clazz = Class.forName(config.getString("class"))
    val constructor = clazz.getConstructor(classOf[Config])
    constructor.newInstance(config).asInstanceOf[Action]
  }

}

trait Action {

  val logger = LoggerFactory.getLogger(this.getClass)

  def execute(normalization : Normalization) : Unit

}

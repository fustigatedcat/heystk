package com.fustigatedcat.heystk.engine.rule

import com.typesafe.config.Config

object Action {

  def create(config : Config): Action = {
    new Action(
      config.getString("id"),
      config
    )
  }

}

class Action(val id : String, val config : Config) {

}

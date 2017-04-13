package com.fustigatedcat.heystk.engine.model

import org.squeryl.Schema

import EngineAPIMode._

object heystk extends Schema {

  val Agent = table[Agent]

  val AgentAuthorization = table[AgentAuthorization]

}

package com.fustigatedcat.heystk.ui.model

import org.squeryl.Schema

import UITypeMode._

object heystk extends Schema {

  val Agent = table[Agent]

  val User = table[User]

  val Privilege = table[Privilege]

  val Role = table[Role]

  val RolePrivilegeMap = table[RolePrivilegeMap]

  val UserRoleMap = table[UserRoleMap]

  val AgentFileReader = table[AgentFileReader]

  val EngineAPI = table[EngineAPI]

  val Engine = table[Engine]

  val agentToAgentFileReaderMapper = oneToManyRelation(Agent, AgentFileReader).via((a,afr) => a.id === afr.agentId)

}


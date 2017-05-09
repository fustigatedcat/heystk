package com.fustigatedcat.heystk.ui.dao

import java.util.UUID

import com.fustigatedcat.heystk.ui.model.{Agent, heystk}
import com.fustigatedcat.heystk.ui.model.UITypeMode._

object AgentDAO {

  def getAgentList : List[Agent] = {
    heystk.Agent.allRows.toList
  }

  def createAgent(agent : Agent) : Agent = {
    heystk.Agent.insert(agent)
  }

  def deleteAgents(ids : List[UUID]) = {
    heystk.Agent.deleteWhere(a => a.id in ids)
  }

  def getAgentById(id : String) : Option[Agent] = {
    val uuid = UUID.fromString(id)
    heystk.Agent.where(a => a.id === uuid).headOption
  }

}

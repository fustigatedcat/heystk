package com.fustigatedcat.heystk.ui.dao

import com.fustigatedcat.heystk.ui.model.{Agent, heystk}
import com.fustigatedcat.heystk.ui.model.UITypeMode._

object AgentDAO {

  def getAgentList : List[Agent] = {
    heystk.Agent.allRows.toList
  }

}

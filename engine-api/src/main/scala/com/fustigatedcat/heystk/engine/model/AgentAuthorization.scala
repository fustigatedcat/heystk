package com.fustigatedcat.heystk.engine.model

import java.sql.Timestamp
import java.util.UUID

import org.squeryl.KeyedEntity

case class AgentAuthorization(id : String,
                              expiry : Timestamp,
                              agent_id : UUID) extends KeyedEntity[String] {

}

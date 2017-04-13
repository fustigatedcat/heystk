package com.fustigatedcat.heystk.engine.model

import java.sql.Timestamp

import org.squeryl.KeyedEntity

case class AgentAuthorization(id : String,
                              expiry : Timestamp,
                              agent_id : Long) extends KeyedEntity[String] {

}

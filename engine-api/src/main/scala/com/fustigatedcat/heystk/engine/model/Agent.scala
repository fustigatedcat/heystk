package com.fustigatedcat.heystk.engine.model

import java.sql.Timestamp
import java.util.UUID

import org.squeryl.KeyedEntity

case class Agent(id : UUID,
                 created : Timestamp,
                 last_updated : Timestamp,
                 name : String,
                 authentication : String) extends KeyedEntity[UUID] {

}

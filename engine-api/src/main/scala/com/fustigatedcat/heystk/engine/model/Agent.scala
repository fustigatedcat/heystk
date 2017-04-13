package com.fustigatedcat.heystk.engine.model

import java.sql.Timestamp

import org.squeryl.KeyedEntity

case class Agent(id : Long,
                 created : Timestamp,
                 last_updated : Timestamp,
                 name : String,
                 authentication : String) extends KeyedEntity[Long] {

}

package com.fustigatedcat.heystk.ui.model

import java.util.UUID

import net.liftweb.json.JsonAST.JValue
import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

object AgentFileReader {

  implicit val formats = net.liftweb.json.DefaultFormats

  def parse(js : JValue) : AgentFileReader  = {
    AgentFileReader(
      0,
      null,
      1000,
      (js \ "filePath").extract[String]
    )
  }

}

case class AgentFileReader(id : Long,
                           @Column("agent_id") agentId : UUID,
                           delay : Long,
                           @Column("file_path") filePath : String) extends KeyedEntity[Long] {

}

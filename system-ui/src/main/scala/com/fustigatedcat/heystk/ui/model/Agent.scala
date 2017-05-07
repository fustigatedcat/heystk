package com.fustigatedcat.heystk.ui.model

import java.sql.Timestamp
import java.util.{Calendar, UUID}

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.parseOpt

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

import scala.util.Random

object Agent {

  implicit val format = net.liftweb.json.DefaultFormats

  def parse(js : JValue) : Agent = {
    Agent(
      UUID.randomUUID(),
      new Timestamp(Calendar.getInstance.getTimeInMillis),
      new Timestamp(Calendar.getInstance.getTimeInMillis),
      (js \ "name").extract[String],
      (js \ "type").extract[String],
      Random.alphanumeric.take(512).mkString
    )
  }

}

case class Agent(id : UUID,
                 created : Timestamp,
                 last_updated : Timestamp,
                 name : String,
                 @Column("agent_type") agentType : String,
                 authentication : String) extends KeyedEntity[UUID] {

  lazy val fileReader = heystk.agentToAgentFileReaderMapper.left(this)

  def parseAndAssociateAgentType(js : JValue) = agentType match {
    case "FILE_READER" => this.fileReader.associate(AgentFileReader.parse(js))
    case _ => throw new IllegalStateException("Invalid type")
  }

}


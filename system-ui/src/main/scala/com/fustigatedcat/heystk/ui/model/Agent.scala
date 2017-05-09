package com.fustigatedcat.heystk.ui.model

import java.sql.Timestamp
import java.util.{Calendar, UUID}

import net.liftweb.json.JsonAST.{JObject, JArray, JValue}

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

import net.liftweb.json.JsonDSL._

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
      (js \ "engineUrl").extract[String],
      (js \ "maxCache").extract[Long],
      (js \ "maxDelay").extract[Long],
      Random.alphanumeric.take(512).mkString
    )
  }

}

case class Agent(id : UUID,
                 created : Timestamp,
                 last_updated : Timestamp,
                 name : String,
                 @Column("agent_type") agentType : String,
                 @Column("engine_url") engineUrl : String,
                 @Column("max_cache") maxCache : Long,
                 @Column("max_delay") maxDelay : Long,
                 authentication : String) extends KeyedEntity[UUID] {

  lazy val fileReader = heystk.agentToAgentFileReaderMapper.left(this)

  def parseAndAssociateAgentType(js : JValue) = agentType match {
    case "FILE_READER" => this.fileReader.associate(AgentFileReader.parse(js))
    case _ => throw new IllegalStateException("Invalid type")
  }

  def getAgentConfig : JObject = agentType match {
    case "FILE_READER" => fileReader.single.toJs
  }

  def toJs : JValue = {
    ("agent" -> (
      ("name" -> this.name) ~
        ("normalizer-system" -> (
          "chains" -> JArray(List())
          )
        ) ~
        getAgentConfig
      )
    ) ~
      ("engine" -> (
        ("url" -> this.engineUrl) ~
          ("key" -> this.authentication) ~
          ("max-cache" -> this.maxCache) ~
          ("max-delay" -> this.maxDelay)
        )
      )
  }

}


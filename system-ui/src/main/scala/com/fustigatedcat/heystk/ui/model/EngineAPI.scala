package com.fustigatedcat.heystk.ui.model

import java.sql.Timestamp
import java.util.Calendar

import net.liftweb.json.JsonAST.JValue
import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

import net.liftweb.json.JsonDSL._

object EngineAPI {

  implicit val formats = net.liftweb.json.DefaultFormats

  def parse(js : JValue) : EngineAPI = {
    EngineAPI(
      0,
      (js \ "host").extract[String],
      (js \ "port").extract[Int],
      (js \ "context").extract[String],
      (js \ "authorization" \ "expiry").extract[Int],
      (js \ "database" \ "host").extract[String],
      (js \ "database" \ "port").extract[Int],
      (js \ "database" \ "db").extract[String],
      (js \ "queue" \ "amqp" \ "host").extract[String],
      (js \ "queue" \ "amqp" \ "port").extract[Int],
      (js \ "queue" \ "amqp" \ "vhost").extract[String],
      (js \ "queue" \ "amqp" \ "api" \ "exchangeName").extract[String],
      (js \ "queue" \ "amqp" \ "api" \ "queueName").extract[String],
      (js \ "queue" \ "amqp" \ "api" \ "routingKey").extract[String],
      new Timestamp(Calendar.getInstance.getTimeInMillis),
      new Timestamp(Calendar.getInstance.getTimeInMillis)
    )
  }

}

case class EngineAPI(id : Long,
                     @Column("listen_host") listenHost : String,
                     @Column("listen_port") listenPort : Int,
                     context : String,
                     @Column("authorization_expiry") authorizationExpiry : Int,
                     @Column("database_host") databaseHost : String,
                     @Column("database_port") databasePort : Int,
                     @Column("database_db") databaseDb : String,
                     @Column("queue_amqp_host") queueAmqpHost : String,
                     @Column("queue_amqp_port") queueAmqpPort : Int,
                     @Column("queue_amqp_vhost") queueAmqpVhost : String,
                     @Column("queue_amqp_api_exchange_name") queueAmqpApiExchangeName : String,
                     @Column("queue_amqp_api_queue_name") queueAmqpApiQueueName : String,
                     @Column("queue_amqp_api_routing_key") queueAmqpApiRoutingKey : String,
                     created : Timestamp,
                     @Column("last_updated") lastUpdated : Timestamp) extends KeyedEntity[Long] {

  def toJs : JValue = {
    ("engine" ->
      ("api" -> (
        ("host" -> this.listenHost) ~
          ("port" -> this.listenPort) ~
          ("context" -> this.context) ~
          ("authorization" -> ("expiry" -> this.authorizationExpiry)) ~
          ("database" -> (
            ("host" -> this.databaseHost) ~
              ("port" -> this.databasePort) ~
              ("db" -> this.databaseDb) ~
              ("username" -> "USERNAME") ~
              ("password" -> "PASSWORD") ~
              ("pool" -> (
                ("min" -> 5) ~
                  ("max" -> 20)
                )) ~
              ("jdbc-properties" -> (
                "serverTimezone" -> "UTC"
                ))
            ))
        )) ~
        ("queue" -> (
          "amqp" -> (
            ("host" -> this.queueAmqpHost) ~
              ("port" -> this.queueAmqpPort) ~
              ("vhost" -> this.queueAmqpVhost) ~
              ("username" -> "USERNAME") ~
              ("password" -> "PASSWORD") ~
              ("api" -> (
                ("exchange-name" -> this.queueAmqpApiExchangeName) ~
                  ("queue-name" -> this.queueAmqpApiQueueName) ~
                  ("routing-key" -> this.queueAmqpApiRoutingKey)
                ))
            )
          ))
      )
  }

}

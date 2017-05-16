package com.fustigatedcat.heystk.ui.model

import java.sql.Timestamp
import java.util.Calendar

import net.liftweb.json.JsonAST.JValue
import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

object Engine {

  implicit val formats = net.liftweb.json.DefaultFormats

  def parse(js : JValue) : Engine = {
    Engine(
      0,
      (js \ "name").extract[String],
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

case class Engine(id : Long,
                  name : String,
                  @Column("queue_amqp_host") queueAmqpHost : String,
                  @Column("queue_amqp_port") queueAmqpPort : Int,
                  @Column("queue_amqp_vhost") queueAmqpVhost : String,
                  @Column("queue_amqp_api_exchange_name") queueAmqpApiExchangeName : String,
                  @Column("queue_amqp_api_queue_name") queueAmqpApiQueueName : String,
                  @Column("queue_amqp_api_routing_key") queueAmqpApiRoutingKey : String,
                  created : Timestamp,
                  @Column("last_updated") lastUpdated : Timestamp) extends KeyedEntity[Long] {

}

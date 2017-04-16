package com.fustigatedcat.heystk.engine.queue

import com.fustigatedcat.heystk.engine.EngineAPI
import com.fustigatedcat.heystk.common.normalization.Normalization
import com.rabbitmq.client.{MessageProperties, BuiltinExchangeType, ConnectionFactory}
import org.json4s.native.Serialization.write
import org.slf4j.LoggerFactory

object RabbitQueue {

  implicit val formats = org.json4s.DefaultFormats

  val logger = LoggerFactory.getLogger(this.getClass)

  val config = EngineAPI.config.getConfig("engine.queue.amqp")

  val apiExchangeName = config.getString("api.exchange-name")

  val apiQueueName = config.getString("api.queue-name")

  val toProcessRoutingKey = config.getString("api.routing-key")

  val factory = {
    val f = new ConnectionFactory()
    f.setHost(config.getString("host"))
    f.setPort(config.getInt("port"))
    f.setVirtualHost(config.getString("vhost"))
    f.setUsername(config.getString("user"))
    f.setPassword(config.getString("password"))
    f
  }

  val connection = factory.newConnection()

  val channel = {
    val c = connection.createChannel()
    c.exchangeDeclare(apiExchangeName, BuiltinExchangeType.DIRECT, true, false, null)
    c.queueDeclare(apiQueueName, true, false, false, null)
    c.queueBind(apiQueueName, apiExchangeName, toProcessRoutingKey)
    c
  }

  def postToProcess(normalization : Normalization) = {
    val norm = write(normalization)
    logger.debug("Publishing normalization {}", norm)
    channel.basicPublish(
      apiExchangeName,
      toProcessRoutingKey,
      MessageProperties.PERSISTENT_TEXT_PLAIN,
      norm.getBytes
    )
  }

}

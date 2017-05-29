package com.fustigatedcat.heystk.engine.queue

import com.fustigatedcat.heystk.engine.Engine
import com.rabbitmq.client._
import org.slf4j.LoggerFactory

object RabbitQueue {

  implicit val formats = org.json4s.DefaultFormats

  val logger = LoggerFactory.getLogger(this.getClass)

  val config = Engine.config.getConfig("engine.queue.amqp")

  val apiQueueName = config.getString("api.queue-name")

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
    c.basicQos(1)
    c
  }

  val consumer = new RabbitQueueConsumer(channel)

  def startConsuming() : Unit= {
    channel.basicConsume(apiQueueName, false, consumer)
  }

}

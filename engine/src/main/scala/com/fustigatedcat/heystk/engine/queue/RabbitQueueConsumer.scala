package com.fustigatedcat.heystk.engine.queue

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.fustigatedcat.heystk.engine.Engine
import com.fustigatedcat.heystk.engine.processor.Processor
import com.rabbitmq.client.{AMQP, Envelope, Channel, DefaultConsumer}
import org.json4s.native.JsonMethods.parse
import org.slf4j.LoggerFactory

class RabbitQueueConsumer(channel : Channel) extends DefaultConsumer(channel) {

  val logger = LoggerFactory.getLogger(this.getClass)

  implicit val formats = org.json4s.DefaultFormats

  val processor = Processor.create(Engine.config)

  override def handleDelivery(consumerTag : String, envelope : Envelope, properties : AMQP.BasicProperties, body : Array[Byte]) : Unit = {
    logger.debug("Processing a new normalization {}", envelope.getDeliveryTag)
    val normalization = parse(new String(body)).extract[Normalization]
    logger.debug("Got normalization {}", normalization)
    processor.process(normalization)
    logger.debug("Acking normalization {}", envelope.getDeliveryTag)
    channel.basicAck(envelope.getDeliveryTag, false)
  }

}

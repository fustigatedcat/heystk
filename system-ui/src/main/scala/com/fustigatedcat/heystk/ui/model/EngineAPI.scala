package com.fustigatedcat.heystk.ui.model

import java.sql.Timestamp

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

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

}

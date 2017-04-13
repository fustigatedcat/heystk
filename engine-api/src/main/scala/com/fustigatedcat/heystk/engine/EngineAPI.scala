package com.fustigatedcat.heystk.engine

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.fustigatedcat.heystk.engine.dao.AgentDAO
import com.fustigatedcat.heystk.engine.model.{AgentAuthorization, Agent}
import com.fustigatedcat.heystk.engine.normalization.NormalizationHandler
import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.ConfigFactory
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.MySQLInnoDBAdapter

import scala.io.StdIn

import org.json4s.native.Serialization.{write, writePretty}

import scala.collection.JavaConverters._

object EngineAPI {

  implicit val defaults = org.json4s.DefaultFormats

  val config = ConfigFactory.load()

  val authorize : Directive1[Agent] = {
    import com.fustigatedcat.heystk.engine.model.EngineAPIMode._
    headerValueByName('Authorization).
      map { a => inTransaction { AgentDAO.authorizeAgent(a) } }.flatMap {
      case Some(agent) => provide(agent)
      case _ => reject
    }
  }

  val authenticate : Directive1[AgentAuthorization] = {
    import com.fustigatedcat.heystk.engine.model.EngineAPIMode._
    entity(as[String]).
      map { body => inTransaction { AgentDAO.authenticateAgent(body) } }.flatMap {
      case Some(auth) => provide(auth)
      case _ => reject
    }
  }

  def setupRoute() = {
    path(config.getString("engine.api.context") / "normalizations") {
      authorize { agent =>
        post {
          entity(as[String]) { body =>
            // b64decode -> gunzip -> post Normalizations
            NormalizationHandler.handle(body)
            complete("")
          }
        }
      }
    } ~
    path(config.getString("engine.api.context") / "authentication") {
      authenticate { authorization =>
        complete(writePretty(authorization))
      }
    }
  }

  def setupC3P0(): ComboPooledDataSource = {
    val dbConfig = config.getConfig("engine.api.database")
    val cpds = new ComboPooledDataSource()
    val props = dbConfig.getConfig("jdbc-properties").entrySet().asScala.map(e => s"${e.getKey}=${e.getValue.unwrapped()}").mkString("&")
    cpds.setJdbcUrl(s"jdbc:mysql://${dbConfig.getString("host")}:${dbConfig.getInt("port")}/${dbConfig.getString("db")}?$props")
    cpds.setUser(dbConfig.getString("username"))
    cpds.setPassword(dbConfig.getString("password"))
    cpds.setMinPoolSize(dbConfig.getInt("pool.min"))
    cpds.setMaxPoolSize(dbConfig.getInt("pool.max"))
    cpds.setDriverClass("com.mysql.cj.jdbc.Driver")
    cpds
  }

  def setupDB(): Unit = {
    val cpds = setupC3P0()
    SessionFactory.concreteFactory = Some(() => Session.create(cpds.getConnection, new MySQLInnoDBAdapter()))
  }

  def main(args : Array[String]) : Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    setupDB()

    val bindingFuture = Http().bindAndHandle(setupRoute(), config.getString("engine.api.host"), config.getInt("engine.api.port"))

    println(s"Server online at http://${config.getString("engine.api.host")}:${config.getInt("engine.api.port")}/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}

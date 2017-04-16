package com.fustigatedcat.heystk.agent.common.engine

import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

import akka.actor.Actor
import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config
import dispatch.Http
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory

import org.json4s.native.parseJson
import org.json4s._
import org.json4s.native.Serialization.writePretty

import dispatch._, Defaults._

class EngineClient(config : Config) extends Actor {

  implicit val formats = org.json4s.DefaultFormats

  var normalizations : List[Normalization] = List()

  val key = config.getString("engine.key")

  val maxCache = config.getInt("engine.max-cache")

  var auth : Option[String] = None

  val post : () => Boolean = if(key.trim.isEmpty) {
    this.unauthenticatedPost
  } else {
    this.realPost
  }

  val logger = LoggerFactory.getLogger(this.getClass)

  def authenticate(cb : () => Boolean) : Boolean = {
    Http(
      url(config.getString("engine.url")) / "authentication" << key
      OK as.String
    ).option() match {
      case Some(resp) => {
        this.auth = Some((parseJson(resp) \\ "id").extract[String])
        cb()
      }
      case _ => {
        this.auth = None
        false
      }
    }
  }

  def zipBody(body : String) : String = {
    val baos = new ByteArrayOutputStream()
    val gzipStream = new GZIPOutputStream(baos)
    gzipStream.write(body.getBytes("UTF-8"))
    gzipStream.close()
    Base64.encodeBase64String(baos.toByteArray)
  }

  def unauthenticatedPost() : Boolean = {
    logger.debug("Not publishing, authorization key not provided {}", zipBody(writePretty(normalizations)))
    normalizations = List()
    true
  }

  def realPost() : Boolean = auth match {
    case Some(a) => {
      val body = writePretty(normalizations)
      val req = (url(config.getString("engine.url")) / "normalizations" << zipBody(body)).
        setHeader("Authorization", a).
        setContentType("application/json", "UTF-8").
        setHeader("Checksum", DigestUtils.sha256Hex(body))
      Http(req OK as.String).option() match {
        case Some(resp) => {
          // we posted, yay!
          normalizations = List()
          true
        }
        case _ => {
          this.auth = None
          authenticate(this.post)
        }
      }
    }
    case _ => {
      authenticate(this.post)
    }
  }

  override def receive = {
    case norm : Normalization => {
      this.normalizations = norm :: this.normalizations
      if(this.normalizations.length >= maxCache) {
        post()
      }
    }
    case 'POST => {
      if(this.normalizations.nonEmpty) {
        post()
      }
    }
  }

}

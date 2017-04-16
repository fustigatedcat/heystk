package com.fustigatedcat.heystk.engine.normalization

import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.fustigatedcat.heystk.engine.queue.RabbitQueue
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

import org.json4s.native.JsonMethods.parse
import org.slf4j.LoggerFactory

object NormalizationHandler {

  val logger = LoggerFactory.getLogger(this.getClass)

  implicit val formats = org.json4s.DefaultFormats

  def checksum(string : String) : String = {
    DigestUtils.sha256Hex(string)
  }

  def unzip(string : String) : String = {
    val arr = Base64.decodeBase64(string)
    val gis = new GZIPInputStream(new ByteArrayInputStream(arr))
    IOUtils.toString(gis, "UTF-8")
  }

  def handle(string : String, chksm : String) = {
    val body = unzip(string)
    if(checksum(body) == chksm) {
      // write normalization to temp DB and post to Queue for needs processing
      parse(unzip(string)).extract[List[Normalization]].par.foreach(RabbitQueue.postToProcess)
    } else {
      logger.error(s"Invalid checksum ${checksum(body)} != $chksm")
    }
  }

}

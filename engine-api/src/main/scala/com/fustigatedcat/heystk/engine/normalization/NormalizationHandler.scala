package com.fustigatedcat.heystk.engine.normalization

import java.io.{ByteArrayInputStream, InputStreamReader, StringWriter}
import java.util.zip.GZIPInputStream

import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils

import org.json4s.native.JsonMethods.parse

object NormalizationHandler {

  implicit val formats = org.json4s.DefaultFormats

  def unzip(string : String) : String = {
    val arr = Base64.decodeBase64(string)
    val gis = new GZIPInputStream(new ByteArrayInputStream(arr))
    IOUtils.toString(gis, "UTF-8")
  }

  def handle(string : String) = {
    val normalizations = parse(unzip(string)).extract[List[Normalization]]
    println(normalizations)
  }

}

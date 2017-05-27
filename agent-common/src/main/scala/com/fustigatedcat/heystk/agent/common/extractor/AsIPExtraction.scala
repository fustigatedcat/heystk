package com.fustigatedcat.heystk.agent.common.extractor

import java.net.InetAddress

class AsIPExtraction(extraction : Extraction) extends Extraction {

  override def process(log: String): (String, String) = {
    val t = extraction.process(log)
    try {
      val inet = new InetAddress(t._2)
      "ip" -> t._2
    } catch {
      case e : Exception => t
    }
  }

}

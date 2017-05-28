package com.fustigatedcat.heystk.agent.common.extractor

class AsIntegerExtraction(extraction : Extraction) extends Extraction {

  override def process(log: String): (String, String) = {
    val t = extraction.process(log)
    try {
      "integer" -> t._2.toInt.toString
    } catch {
      case e : Exception => t
    }
  }

}

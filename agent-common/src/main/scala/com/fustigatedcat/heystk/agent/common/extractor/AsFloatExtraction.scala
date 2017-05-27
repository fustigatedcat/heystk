package com.fustigatedcat.heystk.agent.common.extractor

class AsFloatExtraction(extraction : Extraction) extends Extraction {

  override def process(log: String): (String, String) = {
    val t = extraction.process(log)
    try {
      "float" -> t._2.toFloat.toString
    } catch {
      case e : Exception => t
    }
  }

}

package com.fustigatedcat.heystk.agent.common.extractor

class TextExtraction(value : Extraction) extends Extraction {

  override def process(log: String): (String, String) = {
    "string" -> value.process(log)._2
  }

}

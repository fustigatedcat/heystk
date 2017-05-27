package com.fustigatedcat.heystk.agent.common.extractor

class NumberExtraction(value : Long) extends Extraction {

  override def process(log: String): (String, String) = {
    "string" -> value.toString
  }

}

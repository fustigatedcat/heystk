package com.fustigatedcat.heystk.agent.common.extractor

class StringExtraction(string : String) extends Extraction {

  override def process(log: String): (String, String) = {
    "string" -> string.drop(1).dropRight(1)
  }

}

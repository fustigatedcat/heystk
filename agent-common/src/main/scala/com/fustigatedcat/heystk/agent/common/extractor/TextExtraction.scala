package com.fustigatedcat.heystk.agent.common.extractor

class TextExtraction(value : Extraction) extends Extraction {

  override def process(log: String): String = {
    value.process(log)
  }

}

package com.fustigatedcat.heystk.agent.common.extractor

class ColumnExtraction(sep : Extraction, count : Extraction) extends Extraction {

  override def process(log: String): (String, String) = {
    val cols = log.split(sep.process(log)._2)
    val cnt = count.process(log)._2.toInt
    if(cnt > cols.length) {
      "string" -> ""
    } else {
      "string" -> cols(cnt - 1)
    }
  }

}

package com.fustigatedcat.heystk.agent.common.extractor

class ColumnExtraction(sep : Extraction, count : Extraction) extends Extraction {

  override def process(log: String): String = {
    val cols = log.split(sep.process(log))
    val cnt = count.process(log).toInt
    if(cnt > cols.length) {
      ""
    } else {
      cols(cnt - 1)
    }
  }

}

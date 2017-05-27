package com.fustigatedcat.heystk.agent.common.extractor

class AsIntegerExtraction(extractor : Extraction) extends Extraction {

  override def process(log: String): (String, String) = "integer" -> extractor.process(log)._2

}

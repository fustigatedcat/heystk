package com.fustigatedcat.heystk.agent.common.extractor

class AsStringExtraction(extractor : Extraction) extends Extraction {

  override def process(log: String): (String, String) = "string" -> extractor.process(log)._2

}

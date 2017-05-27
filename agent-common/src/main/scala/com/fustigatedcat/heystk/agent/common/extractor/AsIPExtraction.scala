package com.fustigatedcat.heystk.agent.common.extractor

class AsIPExtraction(extraction : Extraction) extends Extraction {

  override def process(log: String): (String, String) = "ip" -> extraction.process(log)._2

}

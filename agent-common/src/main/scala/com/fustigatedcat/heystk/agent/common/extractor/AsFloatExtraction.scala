package com.fustigatedcat.heystk.agent.common.extractor

class AsFloatExtraction(extraction : Extraction) extends Extraction {

  override def process(log: String): (String, String) = "float" -> extraction.process(log)._2

}

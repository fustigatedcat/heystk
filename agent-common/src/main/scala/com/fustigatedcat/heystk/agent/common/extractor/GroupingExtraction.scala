package com.fustigatedcat.heystk.agent.common.extractor

class GroupingExtraction(extractions : List[Extraction]) extends Extraction {

  override def process(log: String): (String, String) = "string" -> extractions.map(_.process(log)._2).mkString("")

}

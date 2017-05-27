package com.fustigatedcat.heystk.agent.common.normalization

import com.fustigatedcat.heystk.agent.common.extractor.ExtractorParser
import com.fustigatedcat.heystk.common.normalization.{Log, Normalization}
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

object Extractor {

  def create(config : Config) : Extractor = {
    new Extractor(
      config.getString("field"),
      config.getString("parser")
    )
  }

}

class Extractor(val field : String, parser : String) {

  val logger = LoggerFactory.getLogger(this.getClass)

  val extractors = ExtractorParser.parseOpt(parser).getOrElse(List())

  def process(log : Log) : (String, String) = {
    val extraction = extractors.foldLeft("")((o, e) => o + e.process(log.message))
    logger.trace(s"Field [$field] Extraction [$extraction]")
    field -> extraction
  }

}

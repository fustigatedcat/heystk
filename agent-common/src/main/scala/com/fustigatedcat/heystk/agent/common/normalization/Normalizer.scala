package com.fustigatedcat.heystk.agent.common.normalization

import java.util.regex.Pattern

import com.fustigatedcat.heystk.common.normalization.{Log, Normalization}
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

object Normalizer {

  def create(config : Config) : Normalizer = {
    new Normalizer(
      config.getString("name"),
      config.getString("expression"),
      config.getConfigList("extractors").asScala.map(Extractor.create).toList,
      config.getConfigList("child-normalizers").asScala.map(Normalizer.create).toList
    )
  }

}

class Normalizer(name : String, exp : String, extractors : List[Extractor], childNormalizers : List[Normalizer]) {

  val regex = Pattern.compile(exp, Pattern.DOTALL|Pattern.UNIX_LINES|Pattern.MULTILINE)

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(log : Log) : Map[String, Extractor] = {
    if(regex.matcher(log.message).find()) {
      logger.trace("Processing normalizer [{}]", name)
      val rtn : Map[String, Extractor] = childNormalizers.foldLeft(
        extractors.map(e => e.field -> e).toMap[String, Extractor] // handle current extractors
      )((n, c) => n ++ c.process(log)) // collect child extractors
      logger.trace("Done processing normalizer [{}]", name)
      rtn
    } else {
      logger.trace("Skipping normalizer [{}]", name)
      Map()
    }
  }

}

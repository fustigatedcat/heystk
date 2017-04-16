package com.fustigatedcat.heystk.agent.common.normalization

import java.util.regex.Pattern

import com.fustigatedcat.heystk.common.normalization.Normalization
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

  def process(normalization : Normalization) : Normalization = {
    if(regex.matcher(normalization.log.message).find()) {
      logger.trace("Processing normalizer [{}]", name)
      val rtn = childNormalizers.foldLeft(
        extractors.foldLeft(normalization)((n, c) => c.process(n)) // parse fields
      )((n, c) => c.process(n)) // parse children
      logger.trace("Done processing normalizer [{}]", name)
      rtn
    } else {
      logger.trace("Skipping normalizer [{}]", name)
      normalization
    }
  }

}

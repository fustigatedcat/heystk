package com.fustigatedcat.heystk.agent.common.extractor

trait Extraction {

  def process(log : String) : String

}

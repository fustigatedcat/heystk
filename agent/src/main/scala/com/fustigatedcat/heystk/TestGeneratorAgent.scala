package com.fustigatedcat.heystk

import com.fustigatedcat.heystk.agent.common.Agent
import com.fustigatedcat.heystk.agent.common.normalization.Log

object TestGeneratorAgent extends Agent {

  lazy val string = this.config.getString("agent.test-generator.string")

  lazy val count = this.config.getInt("agent.test-generator.count")

  override def init(): Boolean = {
    true
  }

  override def run(): Boolean = {
    while(true) {
      for(c <- 0 until count) {
        this.normalize(Log(string))
      }
      Thread.sleep(60 * 1000)
    }
    true
  }

}

package com.fustigatedcat.heystk

import java.io.File

import com.fustigatedcat.heystk.agent.common.Agent
import org.apache.commons.io.input.Tailer

object FileReaderAgent extends Agent {

  var filePath = ""

  var delay = 1000

  lazy val tailerListener = new AgentTailerListener(this)

  lazy val tailer = new Tailer(
    new File(this.filePath),
    tailerListener,
    this.delay,
    true
  )

  override def init() : Boolean = {
    this.filePath = this.config.getString("agent.file-reader.file-path")
    this.delay = this.config.getInt("agent.file-reader.delay")
    logger.debug("Creating file reader for file [{}] at delay [{}]", this.filePath, this.delay)
    tailer
    true
  }

  override def run(): Boolean = {
    tailer.run()
    true
  }
}

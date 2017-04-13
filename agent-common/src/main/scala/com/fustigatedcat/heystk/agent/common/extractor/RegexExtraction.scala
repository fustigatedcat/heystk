package com.fustigatedcat.heystk.agent.common.extractor

import java.util.regex.Pattern

class RegexExtraction(group : Extraction, expression : Extraction) extends Extraction {

  /* TODO: Pattern.compile is expensive, IFF expression is a StringExtraction we should get it and build the pattern only once. */

  override def process(log: String): String = {
    val g = group.process(log).toInt
    val p = Pattern.compile(expression.process(log), Pattern.DOTALL|Pattern.UNIX_LINES|Pattern.MULTILINE)
    val m = p.matcher(log)
    if(m.find() && g <= m.groupCount()) {
      m.group(g)
    } else {
      ""
    }
  }

}

package com.fustigatedcat.heystk.agent.common.extractor

import org.slf4j.LoggerFactory

import scala.util.parsing.combinator.JavaTokenParsers

/**
  * Extractor options
  *
  * Text(<string>)
  * Col(<seperator>, <col>)
  * Regex(<group>, <expression>)
  */
object ExtractorParser extends JavaTokenParsers {

  val logger = LoggerFactory.getLogger(this.getClass)

  def string : Parser[Extraction] = stringLiteral ^^ { case s => new StringExtraction(s) }

  def number : Parser[Extraction] = wholeNumber ^^ { case n => new NumberExtraction(n.toLong) }

  def stringExtraction : Parser[Extraction] = ( string | text | column | regex ) ^^ { case i => i }

  def numberExtraction : Parser[Extraction] = ( number | text | column | regex ) ^^ { case i => i }

  def text : Parser[Extraction] = "Text(" ~ stringExtraction ~ ")" ^^ { case _ ~ s ~ _ => new TextExtraction(s) }

  def column : Parser[Extraction] = "Column(" ~ stringExtraction ~ "," ~ numberExtraction ~ ")" ^^ { case _ ~ s ~ _ ~ c ~ _ => new ColumnExtraction(s, c) }

  def regex : Parser[Extraction] = "Regex(" ~ numberExtraction ~ "," ~ stringExtraction ~ ")" ^^ { case _ ~ g ~ _ ~ e ~ _ => new RegexExtraction(g, e) }

  def operation : Parser[Extraction] = (text | column | regex) ~ ";" ^^ { case r ~ _ => r }

  def expr : Parser[List[Extraction]] = rep(operation) ^^ { case r => r }

  def parseOpt(str : String) : Option[List[Extraction]] = parseAll(expr, str) match {
    case Success(res, _) => Some(res)
    case NoSuccess(msg, _) => {
      logger.error("Failed to parse Extractor {}", msg)
      None
    }
  }

}

package com.fustigatedcat.heystk.engine.rule

import java.net.InetAddress

import com.fustigatedcat.heystk.common.normalization.Normalization
import com.typesafe.config.Config

import scala.collection.JavaConverters._

object RuleCriterion {

  def create(config : Config) : RuleCriterion = config.getString("type").toLowerCase match {
    case "expression" => createExpression(config)
    case "group" => createGroup(config)
  }

  def createGroup(config : Config) : RuleGroupCriterion = {
    new RuleGroupCriterion(
      RuleGroupCombinator.get(config.getString("combinator")),
      config.getConfigList("criteria").asScala.map(RuleCriterion.create).toList
    )
  }

  def createExpression(config : Config) : RuleExpressionCriterion[_] = {
    val valueType = config.getString("value-type")
    val operator = RuleExpressionOperator.get(config.getString("operator"))
    val values = if(operator.requiresList) {
      getValueListAsType(valueType, config.getStringList("value-list"))
    } else {
      getValueAsType(valueType, config.getString("value")) :: Nil
    }
    new RuleExpressionCriterion(
      config.getString("field"),
      operator,
      getConverter(valueType),
      values
    )
  }

  def getValueAsType(valueType : String, stringValue : String) : T forSome {type T} = valueType.toLowerCase match {
    case "string" => stringValue
    case "integer" => stringValue.toInt
    case "float" => stringValue.toFloat
    case "ip" => InetAddress.getByName(stringValue)
    case _ => null
  }

  def getValueListAsType(valueType : String, stringValue : java.util.List[String]) : List[T forSome{type T}] = valueType.toLowerCase match {
    case "string" => stringValue.asScala.toList
    case "integer" => stringValue.asScala.toList.map(_.toInt)
    case "float" => stringValue.asScala.toList.map(_.toFloat)
    case "ip" => stringValue.asScala.toList.map(InetAddress.getByName)
    case _ => null
  }

  def getConverter(valueType : String) : ((String, String)) => T forSome {type T} = valueType.toLowerCase match {
    case "string" => t => t._2.toString
    case "integer" => t => if(t._1 == "integer") { t._2.toInt } else { throw new IllegalArgumentException(s"${t._2} not integer") }
    case "float" => t => if(t._1 == "float") { t._2.toFloat } else { throw new IllegalArgumentException(s"${t._2} not float") }
    case "ip" => t => if(t._1 == "ip") { InetAddress.getByName(t._2) } else { throw new IllegalArgumentException(s"${t._2} not ip") }
  }

}

trait RuleCriterion {

  def matches(normalization : Normalization) : Boolean

}

class RuleGroupCriterion(combinator : RuleGroupCombinator, criteria : List[RuleCriterion]) extends RuleCriterion {

  def matches(normalization : Normalization) : Boolean = {
    combinator.matches(normalization, criteria)
  }

}

class RuleExpressionCriterion[T](field : String, operator : RuleExpressionOperator, converter : ((String, String)) => T, valueList : List[T]) extends RuleCriterion {

  val valueToSend = if(operator.requiresList) { valueList } else { valueList.head }

  def matches(normalization : Normalization) : Boolean = normalization.fields.get(field) match {
    case Some(normValue) => try {
      operator.matches(converter(normValue), valueToSend)
    } catch {
      case ex : Exception => false
    }
    case _ => false
  }

}
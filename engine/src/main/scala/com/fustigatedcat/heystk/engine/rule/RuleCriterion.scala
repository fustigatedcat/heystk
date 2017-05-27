package com.fustigatedcat.heystk.engine.rule

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
    case "string" if stringValue != null => stringValue
    case _ => null
  }

  def getValueListAsType(valueType : String, stringValue : java.util.List[String]) : List[T forSome{type T}] = valueType.toLowerCase match {
    case "string" => stringValue.asScala.toList
    case _ => null
  }

  def getConverter(valueType : String) : String => T forSome {type T} = valueType.toLowerCase match {
    case "string" => x => x
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

class RuleExpressionCriterion[T](field : String, operator : RuleExpressionOperator, converter : String => T, valueList : List[T]) extends RuleCriterion {

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
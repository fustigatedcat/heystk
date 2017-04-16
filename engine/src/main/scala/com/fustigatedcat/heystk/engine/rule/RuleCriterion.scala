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
    new RuleExpressionCriterion(
      config.getString("field"),
      RuleExpressionOperator.get(config.getString("operator")),
      getConverter(valueType),
      getValueAsType(valueType, config.getString("value"))
    )
  }

  def getValueAsType(valueType : String, stringValue : String) : T forSome {type T} = valueType.toLowerCase match {
    case "string" => stringValue
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

class RuleExpressionCriterion[T](field : String, operator : RuleExpressionOperator[T], converter : String => T, value : T) extends RuleCriterion {

  def matches(normalization : Normalization) : Boolean = normalization.fields.get(field) match {
    case Some(normValue) => try {
      operator.matches(converter(normValue), value)
    } catch {
      case ex : Exception => false
    }
    case _ => false
  }

}
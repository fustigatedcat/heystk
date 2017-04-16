package com.fustigatedcat.heystk.engine.rule

object RuleExpressionOperator {

  def get(string : String) : RuleExpressionOperator[T forSome {type T}] = string.toLowerCase match {
    case "eq" => EqualsRuleExpressionOperator
    case "ne" => NotEqualsRuleExpressionOperator
  }

}

trait RuleExpressionOperator[T] {

  def matches(left : T, right : T) : Boolean

}

object EqualsRuleExpressionOperator extends RuleExpressionOperator[Any] {

  def matches(left : Any, right : Any) : Boolean = {
    left.equals(right)
  }

}

object NotEqualsRuleExpressionOperator extends RuleExpressionOperator[Any] {

  def matches(left : Any, right : Any) : Boolean = {
    !left.equals(right)
  }

}
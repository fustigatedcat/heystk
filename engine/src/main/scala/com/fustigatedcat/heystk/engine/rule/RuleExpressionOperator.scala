package com.fustigatedcat.heystk.engine.rule

object RuleExpressionOperator {

  def get(string : String) : RuleExpressionOperator = string.toLowerCase match {
    case "equals" => EqualsRuleExpressionOperator
    case "not_equals" => NotEqualsRuleExpressionOperator
    case "starts_with" => StartsWithRuleExpressionOperator
    case "ends_with" => EndsWithRuleExpressionOperator
    case "contains" => ContainsRuleExpressionOperator
    case "in" => InRuleExpressionBuilder
    case "not_in" => NotInRuleExpressionBuilder
  }

}

trait RuleExpressionOperator {

  val requiresList = false

  def matches(left : Any, right : Any) : Boolean

  def convert[T](a : Any): Option[T] = a match {
    case s : T => Some(a.asInstanceOf[T])
    case _ => None
  }

}

object EqualsRuleExpressionOperator extends RuleExpressionOperator {

  def matches(left : Any, right : Any) : Boolean = {
    left.equals(right)
  }

}

object NotEqualsRuleExpressionOperator extends RuleExpressionOperator {

  def matches(left : Any, right : Any) : Boolean = {
    !left.equals(right)
  }

}

object StartsWithRuleExpressionOperator extends RuleExpressionOperator {

  def matches(left : Any, right : Any) : Boolean = {
    convert[String](left).exists(l =>
      convert[String](right).exists(r =>
        l.startsWith(r)
      )
    )
  }

}

object EndsWithRuleExpressionOperator extends RuleExpressionOperator {

  def matches(left : Any, right : Any) : Boolean = {
    convert[String](left).exists(l =>
      convert[String](right).exists(r =>
        l.endsWith(r)
      )
    )
  }

}

object ContainsRuleExpressionOperator extends RuleExpressionOperator {

  def matches(left : Any, right : Any) : Boolean = {
    convert[String](left).exists(l =>
      convert[String](right).exists(r =>
        l.contains(r)
      )
    )
  }

}

object InRuleExpressionBuilder extends RuleExpressionOperator {

  override val requiresList = true

  def matches(left : Any, right : Any) : Boolean = {
    convert[List[_]](right).exists(r =>
      r.contains(left)
    )
  }

}

object NotInRuleExpressionBuilder extends RuleExpressionOperator {

  override val requiresList = true

  def matches(left : Any, right : Any) : Boolean = {
    !convert[List[_]](right).exists(r =>
      r.contains(left)
    )
  }

}
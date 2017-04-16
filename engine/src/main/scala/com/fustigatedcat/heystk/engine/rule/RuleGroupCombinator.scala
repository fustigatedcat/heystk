package com.fustigatedcat.heystk.engine.rule

import com.fustigatedcat.heystk.common.normalization.Normalization

object RuleGroupCombinator {

  def get(name : String) : RuleGroupCombinator = name.toLowerCase match {
    case "and" => AndRuleGroupCombinator
    case "or" => OrRuleGroupCombinator
  }

}

trait RuleGroupCombinator {

  def matches(normalization : Normalization, children : List[RuleCriterion]) : Boolean

}

object AndRuleGroupCombinator extends RuleGroupCombinator {

  override def matches(normalization : Normalization, children : List[RuleCriterion]) : Boolean = children match {
    case List() => true
    case c :: cs => if(!c.matches(normalization)) { false } else { matches(normalization, cs) }
  }

}

object OrRuleGroupCombinator extends RuleGroupCombinator {

  override def matches(normalization : Normalization, children : List[RuleCriterion]) : Boolean = children match {
    case List() => false
    case c :: cs => if(c.matches(normalization)) { true } else { matches(normalization, cs) }
  }

}

package com.fustigatedcat.heystk.ui.dao

import org.squeryl.{KeyedEntity, Table}

import com.fustigatedcat.heystk.ui.model.UITypeMode._

trait AbstractDAO[K, A <: KeyedEntity[K]] {

  val table : Table[A]

  def list() : List[A] = {
    table.allRows.toList
  }

  def insert(in : A) : A = {
    table.insert(in)
  }

}

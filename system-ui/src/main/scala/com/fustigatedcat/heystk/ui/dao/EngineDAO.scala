package com.fustigatedcat.heystk.ui.dao

import com.fustigatedcat.heystk.ui.model.{Engine, heystk}

import com.fustigatedcat.heystk.ui.model.UITypeMode._

object EngineDAO extends AbstractDAO[Long, Engine] {

  override val table = heystk.Engine

  def deleteEngines(ids : List[Long]) = {
    heystk.Engine.deleteWhere(api => api.id in ids)
  }

  def getEngineById(id : Long) : Option[Engine] = {
    heystk.Engine.where(engine => engine.id === id).headOption
  }

}

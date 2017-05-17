package com.fustigatedcat.heystk.ui.dao

import com.fustigatedcat.heystk.ui.model.{EngineAPI, heystk}
import com.fustigatedcat.heystk.ui.model.UITypeMode._

object EngineAPIDAO extends AbstractDAO[Long, EngineAPI] {

  override val table = heystk.EngineAPI

  def deleteEngineAPIs(ids : List[Long]) = {
    heystk.EngineAPI.deleteWhere(api => api.id in ids)
  }

  def getEngineAPIById(id : Long) = {
    heystk.EngineAPI.where(api => api.id === id).headOption
  }

}

package com.fustigatedcat.heystk.ui.dao

import com.fustigatedcat.heystk.ui.model.{EngineAPI, heystk}
import com.fustigatedcat.heystk.ui.model.UITypeMode._

object EngineAPIDAO {

  def getEngineAPIList : List[EngineAPI] = {
    heystk.EngineAPI.allRows.toList
  }

  def createEngineAPI(engineAPI : EngineAPI) : EngineAPI = {
    heystk.EngineAPI.insert(engineAPI)
  }

  def deleteEngineAPIs(ids : List[Long]) = {
    heystk.EngineAPI.deleteWhere(api => api.id in ids)
  }

}

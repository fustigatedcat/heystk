package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.EngineDAO
import com.fustigatedcat.heystk.ui.lib.Authorization
import com.fustigatedcat.heystk.ui.model.Engine
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JE.{JsVar, Stringify, JsObj, JsArray}
import net.liftweb.http.js.{JsCmds, JE, JsCmd}
import net.liftweb.json._
import net.liftweb.util._, Helpers._

import scala.xml.NodeSeq

object EngineSnippet {

  implicit val formats = net.liftweb.json.DefaultFormats

  def createEngine() : CssSel = "*" #> Authorization.userAuthorized("CREATE_ENGINE",
    S.attr("callback").map(callback => {
      def _createEngineAPI(js : String) : JsCmd = parseOpt(js).map(js => {
        EngineDAO.insert(Engine.parse(js))
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid Input"))
      JsCmds.Script(
        JsCmds.Function(
          "createEngine",
          List("engine"),
          SHtml.ajaxCall(Stringify(JsVar("engine")), _createEngineAPI).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty),
    NodeSeq.Empty
  )

  def getEngineList() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _getEngineList() : JsCmd = {
      JE.Call(
        callback,
        JsArray(
          EngineDAO.list.map(engine => {
            JsObj(
              "id" -> engine.id,
              "name" -> engine.name,
              "created" -> engine.created.getTime
            )
          })
        )
      ).cmd
    }
    JsCmds.Script(
      JsCmds.Function(
        "getEngineList",
        List(),
        SHtml.ajaxInvoke(_getEngineList).cmd
      )
    )
  }).getOrElse(NodeSeq.Empty)

  def deleteEngines() : CssSel = "*" #> Authorization.userAuthorized("DELETE_ENGINE",
    S.attr("callback").map(callback => {
      def _deleteEngineAPIs(js : String) : JsCmd = parseOpt(js).map(js => {
        EngineDAO.deleteEngines(js.extract[List[Long]])
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid List"))
      JsCmds.Script(
        JsCmds.Function(
          "deleteEngines",
          List("ids"),
          SHtml.ajaxCall(Stringify(JsVar("ids")), _deleteEngineAPIs).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty),
    NodeSeq.Empty
  )

}

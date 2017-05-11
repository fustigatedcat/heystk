package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.EngineAPIDAO
import com.fustigatedcat.heystk.ui.lib.Authorization
import com.fustigatedcat.heystk.ui.model.EngineAPI
import net.liftweb.http.js.JE._
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.util._, Helpers._

import net.liftweb.json._

import scala.xml.NodeSeq

object EngineAPISnippet {

  implicit val formats = net.liftweb.json.DefaultFormats

  def createEngineAPI() : CssSel = "*" #> Authorization.userAuthorized("CREATE_ENGINE_API",
    S.attr("callback").map(callback => {
      def _createEngineAPI(js : String) : JsCmd = parseOpt(js).map(js => {
        EngineAPIDAO.createEngineAPI(EngineAPI.parse(js))
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid Input"))
      JsCmds.Script(
        JsCmds.Function(
          "createEngineAPI",
          List("api"),
          SHtml.ajaxCall(Stringify(JsVar("api")), _createEngineAPI).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty),
    NodeSeq.Empty
  )

  def deleteEngineAPIs() : CssSel = "*" #> Authorization.userAuthorized("DELETE_ENGINE_API",
    S.attr("callback").map(callback => {
      def _deleteEngineAPIs(js : String) : JsCmd = parseOpt(js).map(js => {
        EngineAPIDAO.deleteEngineAPIs(js.extract[List[Long]])
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid List"))
      JsCmds.Script(
        JsCmds.Function(
          "deleteEngineAPIs",
          List("ids"),
          SHtml.ajaxCall(Stringify(JsVar("ids")), _deleteEngineAPIs).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty),
    NodeSeq.Empty
  )

  def getEngineAPIList() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _getEngineAPIList() : JsCmd = {
      JE.Call(
        callback,
        JsArray(
          EngineAPIDAO.getEngineAPIList.map(api => {
            JsObj(
              "id" -> api.id,
              "host" -> api.listenHost,
              "port" -> api.listenPort,
              "context" -> api.context,
              "created" -> api.created.getTime
            )
          })
        )
      ).cmd
    }
    JsCmds.Script(
      JsCmds.Function(
        "getEngineAPIList",
        List(),
        SHtml.ajaxInvoke(_getEngineAPIList).cmd
      )
    )
  }).getOrElse(NodeSeq.Empty)

  def generateConfig() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _generateConfig(js : String) : JsCmd = parseOpt(js).map(id => {
      EngineAPIDAO.getEngineAPIById(id.extract[Long]).map(api => {
        JE.Call(
          callback,
          Str(prettyRender(api.toJs))
        ).cmd
      }).getOrElse(JsCmds.Alert("Invalid EngineAPI"))
    }).getOrElse(JsCmds.Alert("Invalid input"))
    JsCmds.Script(
      JsCmds.Function(
        "generateConfig",
        List("id"),
        SHtml.ajaxCall(Stringify(JsVar("id")), _generateConfig).cmd
      )
    )
  }).getOrElse(NodeSeq.Empty)

}

package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.AgentDAO
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JE.{JsArray, JsObj}
import net.liftweb.http.js.{JsCmds, JE, JsCmd}
import net.liftweb.util._, Helpers._

import scala.xml.NodeSeq

object AgentSnippet {

  def getAgentList() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _getAgentList() : JsCmd = {
      JE.Call(
        callback,
        JsArray(
          AgentDAO.getAgentList.map(agent => {
            JsObj(
              "id" -> agent.id,
              "name" -> agent.name,
              "created" -> agent.created.getTime,
              "authentication" -> agent.authentication
            )
          })
        )
      ).cmd
    }
    JsCmds.Script(
      JsCmds.Function(
        "getAgentList",
        List(),
        SHtml.ajaxInvoke(_getAgentList).cmd
      )
    )
  }).getOrElse(NodeSeq.Empty)

}

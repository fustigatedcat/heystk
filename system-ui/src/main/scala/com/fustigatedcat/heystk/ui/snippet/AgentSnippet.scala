package com.fustigatedcat.heystk.ui.snippet

import java.util.UUID

import com.fustigatedcat.heystk.ui.dao.AgentDAO
import com.fustigatedcat.heystk.ui.lib.Authorization
import com.fustigatedcat.heystk.ui.model.Agent
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JE._
import net.liftweb.http.js.{JsCmds, JE, JsCmd}
import net.liftweb.util._, Helpers._

import net.liftweb.json.parseOpt
import net.liftweb.json.prettyRender

import scala.xml.NodeSeq

object AgentSnippet {

  implicit val formats = net.liftweb.json.DefaultFormats

  def createAgent() : CssSel = "*" #> Authorization.userAuthorized("CREATE_AGENT",
    S.attr("callback").map(callback => {
      def _createAgent(js : String) : JsCmd = parseOpt(js).map(js => {
        AgentDAO.insert(Agent.parse(js)).parseAndAssociateAgentType(js)
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid input"))
      JsCmds.Script(
        JsCmds.Function(
          "createAgent",
          List("agent"),
          SHtml.ajaxCall(Stringify(JsVar("agent")), _createAgent).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty),
    NodeSeq.Empty
  )

  def deleteAgents() : CssSel = "*" #> Authorization.userAuthorized("DELETE_AGENT",
    S.attr("callback").map(callback => {
      def _deleteAgents(js : String) : JsCmd = parseOpt(js).map(js => {
        AgentDAO.deleteAgents(js.extract[List[String]].map(UUID.fromString))
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid List"))
      JsCmds.Script(
        JsCmds.Function(
          "deleteAgents",
          List("ids"),
          SHtml.ajaxCall(Stringify(JsVar("ids")), _deleteAgents).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty),
    NodeSeq.Empty
  )

  def getAgentList() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _getAgentList() : JsCmd = {
      JE.Call(
        callback,
        JsArray(
          AgentDAO.list.map(agent => {
            JsObj(
              "id" -> agent.id.toString,
              "name" -> agent.name,
              "created" -> agent.created.getTime,
              "authentication" -> agent.authentication,
              "type" -> agent.agentType
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

  def generateConfig() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _generateConfig(js : String) : JsCmd = parseOpt(js).map(id => {
      AgentDAO.getAgentById(id.extract[String]).map(agent => {
        JE.Call(
          callback,
          Str(prettyRender(agent.toJs))
        ).cmd
      }).getOrElse(JsCmds.Alert("Invalid Agent"))
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

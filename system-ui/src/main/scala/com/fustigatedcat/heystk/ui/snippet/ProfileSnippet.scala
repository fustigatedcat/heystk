package com.fustigatedcat.heystk.ui.snippet

import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.{JsCmd, JsCmds, JE}
import net.liftweb.http.js.JE.JsObj
import net.liftweb.util._, Helpers._

import scala.xml.NodeSeq

object ProfileSnippet {

  def getLoggedInUser() : CssSel = "*" #> S.attr("callback").map(callback => {
    def _getLoggedInUser() : JsCmd = {
      LoggedInUser.is.map(user => {
        JE.Call(
          callback,
          JsObj(
            "username" -> user.username,
            "firstName" -> user.firstName,
            "lastName" -> user.lastName
          )
        ).cmd
      }).getOrElse(JsCmds.Alert("Non-Existent User"))
    }
    JsCmds.Script(
      JsCmds.Function(
        "getLoggedInUser",
        List(),
        SHtml.ajaxInvoke(_getLoggedInUser).cmd
      )
    )
  }).getOrElse(NodeSeq.Empty)

}

package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.UserDAO
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.{JsCmd, JsCmds, JE}
import net.liftweb.http.js.JE.{JsVar, Stringify, JsObj}
import net.liftweb.json._
import net.liftweb.util._, Helpers._

import scala.xml.NodeSeq

object ProfileSnippet {

  implicit val formats = net.liftweb.json.DefaultFormats

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

  def updateUser() : CssSel = "*" #> {
    def _updateUser(in : String) : JsCmd = {
      LoggedInUser.is.map(user => parseOpt(in).map(js => {
        UserDAO.updateUser(
          user,
          (js \ "firstName").extract[String],
          (js \ "lastName").extract[String],
          (js \ "password").extractOpt[String]
        )
        S.redirectTo("/")
      }).getOrElse(JsCmds.Alert("Invalid Input"))
      ).getOrElse(JsCmds.Alert("Non-Existent User"))
    }
    JsCmds.Script(
      JsCmds.Function(
        "updateUser",
        List("user"),
        SHtml.ajaxCall(Stringify(JsVar("user")), _updateUser).cmd
      )
    )
  }

}

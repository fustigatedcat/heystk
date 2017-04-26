package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.UserDAO
import net.liftweb.http.js.JE.{JsArray, JsObj}
import net.liftweb.http.js.{JsCmd, JE, JsCmds}
import net.liftweb.http.{SHtml, S, RequestVar}
import net.liftweb.util._, Helpers._

import net.liftweb.json.JsonDSL._

import scala.xml.NodeSeq

object UserPrivileges extends RequestVar[List[String]]({
  LoggedInUser.is.map(UserDAO.getPrivilegesForUser).getOrElse(List())
})

object UserSnippet {

  val adminMenuPrivileges = List("VIEW_USERS")

  def ifAdminMenuAvailable(xhtml : NodeSeq) : NodeSeq = {
    if(adminMenuPrivileges.exists(UserPrivileges.is.contains)) {
      xhtml
    } else {
      NodeSeq.Empty
    }
  }

  def ifCanViewUsers(xhtml : NodeSeq) : NodeSeq = {
    if(UserPrivileges.is.contains("VIEW_USERS")) {
      xhtml
    } else {
      NodeSeq.Empty
    }
  }

  def loggedInUser() : CssSel = "#name" #> LoggedInUser.is.map(u => {
    if(u.lastName.isEmpty) { u.firstName } else { s"${u.firstName} ${u.lastName}" }
  }).getOrElse("Unknown")

  def getUserList() : CssSel = "*" #> (if(UserPrivileges.is.contains("VIEW_USERS")) {
    S.attr("callback").map(callback => {
      def _getUserList(string : String) : JsCmd = {
        JE.Call(
          callback,
          JsArray(UserDAO.getUserList().map(u =>
            JsObj("id" -> u.id,
              "username" -> u.username,
              "firstName" -> u.firstName,
              "lastName" -> u.lastName
            )
          ))
        ).cmd
      }
      JsCmds.Script(
        JsCmds.Function(
          "getUserList",
          List(),
          SHtml.ajaxCall(JE.JsNull, _getUserList).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty)
  } else {
    NodeSeq.Empty
  })

}

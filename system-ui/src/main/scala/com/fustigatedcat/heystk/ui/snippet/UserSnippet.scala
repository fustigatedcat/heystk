package com.fustigatedcat.heystk.ui.snippet

import java.text.SimpleDateFormat

import com.fustigatedcat.heystk.ui.dao.UserDAO
import com.fustigatedcat.heystk.ui.model.User
import net.liftweb.json.parseOpt
import net.liftweb.http.js.JE._
import net.liftweb.http.js.{JsCmd, JE, JsCmds}
import net.liftweb.http.{GUIDJsExp, SHtml, S, RequestVar}
import net.liftweb.util._, Helpers._

import net.liftweb.json.JsonDSL._

import scala.xml.NodeSeq

object UserPrivileges extends RequestVar[List[String]]({
  LoggedInUser.is.map(UserDAO.getPrivilegesForUser).getOrElse(List())
})

object UserSnippet {

  implicit val formats = net.liftweb.json.DefaultFormats

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

  def checkUserNameExists() : CssSel = "*" #> (if(UserPrivileges.is.contains("CREATE_USER")) {
    S.attr("callback").map(callback => {
      def _doesUserNameExist(name : String) : JsCmd = {
        JE.Call(
          callback,
          UserDAO.getUserByUsername(name).map(_ => JsTrue).getOrElse(JsFalse)
        ).cmd
      }
      val GUIDJsExp(_, js) = SHtml.ajaxCall(JsVar("username"), _doesUserNameExist)
      JsCmds.Script(JsCmds.Function("checkUserNameExists", List("username"), js.cmd))
    }).getOrElse(NodeSeq.Empty)
  } else {
    NodeSeq.Empty
  })

  def getUserList() : CssSel = "*" #> (if(UserPrivileges.is.contains("VIEW_USERS")) {
    S.attr("callback").map(callback => {
      def _getUserList() : JsCmd = {
        JE.Call(
          callback,
          JsArray(UserDAO.getUserList().map(u =>
            JsObj("id" -> u.id,
              "username" -> u.username,
              "firstName" -> u.firstName,
              "lastName" -> u.lastName,
              "created" -> u.created.getTime
            )
          ))
        ).cmd
      }
      JsCmds.Script(
        JsCmds.Function(
          "getUserList",
          List(),
          SHtml.ajaxInvoke(_getUserList).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty)
  } else {
    NodeSeq.Empty
  })

  def createUser() : CssSel = "*" #> (if(UserPrivileges.is.contains("CREATE_USER")) {
    S.attr("callback").map(callback => {
      def _createUser(js : String) : JsCmd = parseOpt(js).map(js => {
        UserDAO.createUser(
          (js \ "username").extract[String],
          (js \ "firstName").extract[String],
          (js \ "lastName").extract[String],
          (js \ "password").extract[String]
        )
        JE.Call(callback).cmd
      }).getOrElse(JsCmds.Alert("Invalid User Object"))
      JsCmds.Script(
        JsCmds.Function(
          "createUser",
          List("user"),
          SHtml.ajaxCall(Stringify(JsVar("user")), _createUser).cmd
        )
      )
    }).getOrElse(NodeSeq.Empty)
  } else {
    NodeSeq.Empty
  })

}

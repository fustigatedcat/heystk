package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.UserDAO
import net.liftweb.http.RequestVar
import net.liftweb.util._, Helpers._

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

}

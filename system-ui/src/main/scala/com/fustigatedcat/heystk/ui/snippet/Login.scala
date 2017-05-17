package com.fustigatedcat.heystk.ui.snippet

import com.fustigatedcat.heystk.ui.dao.UserDAO
import com.fustigatedcat.heystk.ui.model.User
import net.liftweb.common.Full
import net.liftweb.http.{SessionVar, S}

import scala.xml.NodeSeq

object LoggedInUser extends SessionVar[Option[User]](None)

object Login {

  def login(xhtml : NodeSeq): NodeSeq = if(S.post_?) {
    (S.param("username"), S.param("password")) match {
      case (Full(username), Full(password)) => UserDAO.authenticateUser(username, password).map(u => {
        LoggedInUser(Some(u))
        S.redirectTo("/")
      }).getOrElse(xhtml)
      case _ => xhtml
    }
  } else {
    NodeSeq.Empty
  }

  def logout() = {
    LoggedInUser(None)
    S.session.foreach(_.destroySession())
    S.redirectTo("/login")
  }

}

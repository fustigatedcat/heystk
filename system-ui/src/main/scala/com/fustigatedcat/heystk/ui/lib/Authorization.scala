package com.fustigatedcat.heystk.ui.lib

import com.fustigatedcat.heystk.ui.dao.UserDAO
import com.fustigatedcat.heystk.ui.snippet.LoggedInUser
import net.liftweb.http.RequestVar

object UserPrivileges extends RequestVar[List[String]]({
  LoggedInUser.is.map(UserDAO.getPrivilegesForUser).getOrElse(List())
})

object Authorization {

  def userHasPrivilege(priv : String) : Boolean = {
    UserPrivileges.is.contains(priv)
  }

  def userAuthorized[T](priv : String, onTrue : => T, onFalse : => T) : T = {
    if(userHasPrivilege(priv)) {
      onTrue
    } else {
      onFalse
    }
  }

  def userAuthorized[T](privs : List[String], onTrue : => T, onFalse : => T) : T = {
    if(privs.exists(userHasPrivilege)) {
      onTrue
    } else {
      onFalse
    }
  }

}

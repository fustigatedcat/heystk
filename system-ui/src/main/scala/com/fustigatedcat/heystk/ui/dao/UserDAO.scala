package com.fustigatedcat.heystk.ui.dao

import java.sql.Timestamp
import java.util.Calendar

import com.fustigatedcat.heystk.ui.model.{Privilege, heystk, User}

import com.fustigatedcat.heystk.ui.model.UITypeMode._
import org.apache.commons.codec.digest.DigestUtils

import scala.util.Random

object UserDAO extends AbstractDAO[Long, User] {

  override val table = heystk.User

  def createSalt() : String = Random.alphanumeric.take(64).mkString

  def hash(password : String, salt : String) : String = {
    DigestUtils.sha256Hex(password + salt)
  }

  def authenticate(password : String)(user : User) : Boolean = {
    hash(password, user.salt) == user.hash
  }

  def authenticateUser(username : String, password : String) : Option[User] = {
    from(heystk.User)(u =>
      where(u.username === username)
        select u
    ).headOption.filter(authenticate(password))
  }

  def getPrivilegesForUser(user : User) : List[String] = {
    join(heystk.UserRoleMap,heystk.RolePrivilegeMap,heystk.Privilege)((urm,rpm,p) =>
      where(urm.userId === user.id)
        select p.name
        on(urm.roleId === rpm.roleId, rpm.privilegeId === p.id)
    ).distinct.toList
  }

  def getUserByUsername(username : String) : Option[User] = {
    table.where(u => u.username === username).headOption
  }

  def createUser(username : String, firstName : String, lastName : String, password : String) : User = {
    val salt = createSalt()
    this.insert(
      User(
        0,
        username,
        firstName,
        lastName,
        salt,
        hash(password, salt),
        new Timestamp(Calendar.getInstance.getTimeInMillis)
      )
    )
  }

  def updateUser(user : User, firstName : String, lastName : String, password: Option[String]) = {
    val (s,p) = password.map(str => {
      val salt = createSalt()
      (salt, hash(str, salt))
    }).getOrElse((user.salt, user.hash))
    heystk.User.update(
      User(
        user.id,
        user.username,
        firstName,
        lastName,
        s,
        p,
        user.created
      )
    )
  }

  def deleteUsers(ids : List[Long]) : Unit = {
    table.deleteWhere(u => u.id in ids)
  }

}

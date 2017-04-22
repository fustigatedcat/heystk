package com.fustigatedcat.heystk.ui.dao

import com.fustigatedcat.heystk.ui.model.{heystk, User}

import com.fustigatedcat.heystk.ui.model.UITypeMode._
import org.apache.commons.codec.digest.DigestUtils

import scala.util.Random

object UserDAO {

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

}

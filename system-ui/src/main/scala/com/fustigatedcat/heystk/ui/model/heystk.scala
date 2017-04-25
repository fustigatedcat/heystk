package com.fustigatedcat.heystk.ui.model

import org.squeryl.Schema

import UITypeMode._

object heystk extends Schema {

  val Agent = table[Agent]

  val User = table[User]

  val Privilege = table[Privilege]

  val Role = table[Role]

  val RolePrivilegeMap = table[RolePrivilegeMap]

  val UserRoleMap = table[UserRoleMap]

}


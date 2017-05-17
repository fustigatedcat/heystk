package com.fustigatedcat.heystk.ui.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.dsl.CompositeKey2
import UITypeMode._

case class UserRoleMap(@Column("user_id") userId : Long,
                       @Column("role_id") roleId : Long) extends KeyedEntity[CompositeKey2[Long,Long]] {

  def id = compositeKey(userId, roleId)

}

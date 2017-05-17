package com.fustigatedcat.heystk.ui.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.dsl.CompositeKey2
import UITypeMode._

case class RolePrivilegeMap(@Column("role_id") roleId : Long,
                            @Column("privilege_id") privilegeId : Long) extends KeyedEntity[CompositeKey2[Long,Long]] {

  def id = compositeKey(roleId, privilegeId)

}

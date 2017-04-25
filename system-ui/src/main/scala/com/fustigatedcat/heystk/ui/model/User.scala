package com.fustigatedcat.heystk.ui.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column

case class User(id : Long,
                username : String,
                @Column("first_name") firstName : String,
                @Column("last_name") lastName : String,
                salt : String,
                hash : String) extends KeyedEntity[Long] {

}

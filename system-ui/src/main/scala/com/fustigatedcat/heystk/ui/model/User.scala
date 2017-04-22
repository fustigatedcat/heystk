package com.fustigatedcat.heystk.ui.model

import org.squeryl.KeyedEntity

case class User(id : Long,
                username : String,
                salt : String,
                hash : String) extends KeyedEntity[Long] {

}

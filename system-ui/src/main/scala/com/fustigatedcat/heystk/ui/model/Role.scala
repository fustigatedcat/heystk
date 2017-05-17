package com.fustigatedcat.heystk.ui.model

import org.squeryl.KeyedEntity

case class Role(id : Long,
                name : String) extends KeyedEntity[Long] {

}

package com.fustigatedcat.heystk.ui.model

import org.squeryl.KeyedEntity

case class Privilege(id : Long,
                     name : String) extends KeyedEntity[Long] {

}

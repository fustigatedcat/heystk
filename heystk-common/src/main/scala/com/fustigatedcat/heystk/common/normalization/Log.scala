package com.fustigatedcat.heystk.common.normalization

import java.util.Date

case class Log(message : String, collected : Long = new Date().getTime) {

}

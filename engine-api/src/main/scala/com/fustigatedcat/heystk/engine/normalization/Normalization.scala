package com.fustigatedcat.heystk.engine.normalization

import java.util.Date

case class Normalization(log : Log,
                         startProcessing : Long,
                         endProcessing : Long = new Date().getTime,
                         fields : Map[String, String] = Map()) {
}

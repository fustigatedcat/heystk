package com.fustigatedcat.heystk.engine.dao

import java.sql.Timestamp
import java.util.Calendar

import com.fustigatedcat.heystk.engine.EngineAPI
import com.fustigatedcat.heystk.engine.model.{AgentAuthorization, Agent, heystk}

import com.fustigatedcat.heystk.engine.model.EngineAPIMode._

import scala.util.Random

object AgentDAO {

  val authorizationExpiry = EngineAPI.config.getInt("engine.api.authorization.expiry") * 60 * 1000

  def authorizeAgent(auth : String) : Option[Agent] = {
    val current = new Timestamp(Calendar.getInstance().getTimeInMillis)
    val newExpiry = new Timestamp(Calendar.getInstance().getTimeInMillis + authorizationExpiry)
    join(heystk.Agent,heystk.AgentAuthorization)((a, aa) =>
      where((aa.id === auth) and (aa.expiry gt current))
        select(a,aa)
        on(a.id === aa.agent_id)
    ).map(i => {
      heystk.AgentAuthorization.update(i._2.copy(expiry = newExpiry))
      i._1
    }).headOption
  }

  def authenticateAgent(auth : String) : Option[AgentAuthorization] = {
    val current = new Timestamp(Calendar.getInstance().getTimeInMillis)
    val newExpiry = new Timestamp(Calendar.getInstance().getTimeInMillis + authorizationExpiry)
    heystk.Agent.where(a => a.authentication === auth).headOption.map(a => {
      heystk.AgentAuthorization.where(aa =>
        (aa.agent_id === a.id) and (aa.expiry gt current)
      ).headOption match {
        case Some(aa) => {
          val newaa = aa.copy(expiry = newExpiry)
          heystk.AgentAuthorization.update(newaa)
          newaa
        }
        case _ => heystk.AgentAuthorization.insert(AgentAuthorization(Random.alphanumeric.take(64).mkString, newExpiry, a.id))
      }
    })
  }

}

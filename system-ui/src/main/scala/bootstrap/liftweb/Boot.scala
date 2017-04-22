package bootstrap.liftweb

import java.util.TimeZone

import com.mchange.v2.c3p0.ComboPooledDataSource
import net.liftweb.common.Full
import net.liftweb.http.{S, LiftRules, Req, Html5Properties}
import net.liftweb.http.provider.HTTPRequest
import net.liftweb.sitemap._
import net.liftweb.util.{LiftFlowOfControlException, LoanWrapper, Props}
import org.slf4j.LoggerFactory
import org.squeryl.adapters.MySQLAdapter
import org.squeryl.{Session, SessionFactory}

import com.fustigatedcat.heystk.ui.model.UITypeMode._

class Boot {

  val logger = LoggerFactory.getLogger(classOf[Boot])

  def setupConnectionPool = {
    val cpds = new ComboPooledDataSource()
    cpds.setDriverClass("com.mysql.jdbc.Driver")
    cpds.setJdbcUrl(Props.get("database.jdbc.url").openOrThrowException("database.jdbc.url Required"))
    cpds.setUser(Props.get("database.username").openOrThrowException("database.username Required"))
    cpds.setPassword(Props.get("database.password").openOrThrowException("database.password Required"))
    cpds.setMinPoolSize(Props.getInt("database.pool.min").openOr(5))
    cpds.setAcquireIncrement(5)
    cpds.setMaxPoolSize(Props.getInt("database.pool.max").openOr(20))
    cpds.setTestConnectionOnCheckout(true)
    cpds
  }

  def setupDatabase : Boot = {
    val dataSource = setupConnectionPool
    SessionFactory.concreteFactory = Some(() =>
      Session.create(dataSource.getConnection, new MySQLAdapter())
    )

    S.addAround(new LoanWrapper {
      override def apply[T](f: => T): T = {
        val resultOrExcept = inTransaction {
          Session.currentSession.setLogger(logger.debug)
          try {
            Right(f)
          } catch {
            case e: LiftFlowOfControlException => Left(e)
          }
        }

        resultOrExcept match {
          case Right(result) => result
          case Left(except) => throw except
        }
      }
    })
    this
  }

  def setupSiteMap : Boot = {
    LiftRules.setSiteMap(SiteMap(
      Menu("Home") / "index",
      Menu("Login") / "login",
      Menu("Static") / "static" / **
    ))
    this
  }

  def setupMisc : Boot = {
    // where to search snippet
    LiftRules.addToPackages("com.fustigatedcat.heystk.ui")

    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    //LiftRules.loggedInTest = Full(() => LoggedInUser.is.isDefined)

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    LiftRules.htmlProperties.default.set((r : Req) => new Html5Properties(r.userAgent))

    this
  }

  def boot() : Unit = {
    setupDatabase.setupSiteMap.setupMisc
  }

  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

}

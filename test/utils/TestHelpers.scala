package utils

import akka.actor.ActorSystem
import org.scalatestplus.play.guice.GuiceOneAppPerTest

import scala.concurrent.ExecutionContext

trait TestHelpers {
  _: GuiceOneAppPerTest =>

  implicit lazy val actorSystem: ActorSystem = app.actorSystem
  implicit lazy val ec: ExecutionContext = app.actorSystem.dispatcher
}

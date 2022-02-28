package services

import akka.NotUsed
import akka.actor.{Actor, ActorSystem}
import akka.stream.scaladsl.{Flow, Sink, Source}
import db.WeatherInfoDatabaseComponent
import models.{LocationLimit, WeatherInfo}
import play.api.Configuration
import services.WeatherPolling.PollService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

object WeatherPolling {

  trait Service {
    def getWeatherInfo(location: String): Future[List[WeatherInfo]]
  }

  case object PollService
}

@Singleton
class WeatherPolling @Inject() (
                                 val config: Configuration,
                                 pollingService: WeatherPolling.Service,
                                 weatherDbComponent: WeatherInfoDatabaseComponent,
                               )(implicit ec: ExecutionContext, actorSystem: ActorSystem)
  extends Actor with BaseWeatherConfigHelper {

  override def receive: Receive = {
    case PollService =>

      Source
        .fromIterator(() => locations.iterator)
        .via(getWeatherInfoForSingleLocation)
        .via(saveWeatherInfo())
        .runWith(Sink.foreach(Console.println))
        .recover { th =>
          th.printStackTrace()
        }
  }

  def getWeatherInfoForSingleLocation: Flow[LocationLimit, WeatherInfo, NotUsed] =
    Flow[LocationLimit].flatMapConcat(loc =>
      Source.futureSource(
        pollingService
          .getWeatherInfo(loc.location)
          .map(loc2 => Source.fromIterator(() => loc2.iterator))
      )
    )

  def saveWeatherInfo(): Flow[WeatherInfo, Long, NotUsed] =
    Flow[WeatherInfo].mapAsync(1)(weatherDbComponent.create)
}

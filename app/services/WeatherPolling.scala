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
                                 val pollingService: WeatherPolling.Service,
                                 val weatherDbComponent: WeatherInfoDatabaseComponent,
                               )(implicit val ec: ExecutionContext, actorSystem: ActorSystem)
  extends Actor with BaseWeatherConfigHelper with WeatherPollingFunctions {

  override def receive: Receive = {
    case PollService =>

      Source
        .fromIterator(() => locations.iterator)
        .via(getWeatherInfoForSingleLocation)
        .via(filterByLocationLimit())
        .map(_._1)
        .via(saveWeatherInfo())
        .runWith(Sink.foreach(Console.println)) // not interested in doing anything with the result here
        .recover { th =>
          th.printStackTrace()
        }
  }
}

trait WeatherPollingFunctions {
  type WeatherLimitPair = (WeatherInfo, LocationLimit)

  val pollingService: WeatherPolling.Service
  val weatherDbComponent: WeatherInfoDatabaseComponent
  implicit val ec: ExecutionContext

  def getWeatherInfoForSingleLocation: Flow[LocationLimit, WeatherLimitPair, NotUsed] =
    Flow[LocationLimit].flatMapConcat(loc =>
      Source.futureSource(
        pollingService
          .getWeatherInfo(loc.location)
          .map(loc2 => Source.fromIterator(() => loc2.map(_ -> loc).iterator))
      )
    )

  def filterByLocationLimit(): Flow[WeatherLimitPair, WeatherLimitPair, NotUsed] =
    Flow[WeatherLimitPair].filter {
      case (info, limit) => info.temp >= limit.limit
    }

  def saveWeatherInfo(): Flow[WeatherInfo, Int, NotUsed] =
    Flow[WeatherInfo].mapAsync(1)(weatherDbComponent.create)
}

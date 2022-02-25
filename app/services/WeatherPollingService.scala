package services

import akka.actor.Actor
import models.WeatherInfo

import javax.inject.Singleton
import scala.concurrent.Future

trait WeatherPollingService {

  def getWeatherInfo(location: String): Future[List[WeatherInfo]]
}

@Singleton
object WeatherPollingService extends Actor {
  override def receive: Receive = ???
}

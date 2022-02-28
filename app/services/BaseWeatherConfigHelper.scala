package services

import models.LocationLimit
import play.api.Configuration

import scala.concurrent.duration.FiniteDuration

trait BaseWeatherConfigHelper {
  val config: Configuration

  lazy val locations: Seq[LocationLimit] = config
    .get[Seq[String]]("locations")
    .flatMap {
      _.split(',').toList match {
        case List(location, temperature) =>
          temperature.toDoubleOption.map(LocationLimit(location, _))
        case _ => None
      }
    }

  lazy val pollingInterval = config.get[FiniteDuration]("weather_service.polling_interval")
}

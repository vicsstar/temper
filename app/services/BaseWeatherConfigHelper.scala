package services

import models.LocationLimit
import play.api.Configuration

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
}

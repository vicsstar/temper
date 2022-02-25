package services.impl

import models.WeatherInfo
import play.api.Configuration
import play.api.libs.ws.WSClient
import services.{BaseWeatherConfigHelper, WeatherPollingService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OpenWeatherMapPollingService @Inject() (val config: Configuration, val ws: WSClient)
                                             (implicit val ec: ExecutionContext)
  extends WeatherPollingService with OpenWeatherMapConfigHelper with OpenWeatherMapFunctions {

  override def getWeatherInfo(location: String): Future[List[WeatherInfo]] = {
    for {
      geo       <- getGeolocation(location)
      forecast  <- getForecast(geo)
    } yield forecast.toList
  }
}

trait OpenWeatherMapConfigHelper extends BaseWeatherConfigHelper {

  lazy val owmConfig  = config.get[Configuration]("weather_api.openweathermap")
  lazy val apiKey     = owmConfig.get[String]("api_key")

  val getGeocodingURL = "https://api.openweathermap.org/geo/1.0/direct"
  val getForecastURL  = "https://api.openweathermap.org/data/2.5/forecast"

  object Param {

    val AppID = "appid"
    val City  = "city"
    val Lat   = "lat"
    val Lon   = "lon"
    val Limit = "limit"
    val Q     = "q"
    val Units = "units"
  }
}

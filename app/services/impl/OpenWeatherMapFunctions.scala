package services.impl

import models.WeatherInfo
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, __}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

trait OpenWeatherMapFunctions {
  _: OpenWeatherMapPollingService =>

  implicit val ec: ExecutionContext

  type Latitude     = BigDecimal
  type Longitude    = BigDecimal
  type GeoLocation  = (Latitude, Longitude)

  protected def getGeolocation(location: String): Future[GeoLocation] = {
    ws.url(getGeocodingURL)
      .withRequestTimeout(10000.millis)
      .withQueryStringParameters(
        Param.Q     -> location,
        Param.AppID -> apiKey,
        Param.Limit -> "1",
      )
      .get()
      .map { response =>
        val geoLocationReads = (
          (__ \ "lat").read[BigDecimal] and
          (__ \ "lon").read[BigDecimal]
        )(Tuple2.apply[BigDecimal, BigDecimal] _)

        response.json.as[List[JsValue]].head.as[GeoLocation](geoLocationReads)
      }
  }

  protected def getForecast(geoLocation: GeoLocation): Future[List[WeatherInfo]] = {
    val (latitude, longitude) = geoLocation

    ws.url(getForecastURL)
      .withRequestTimeout(10000.millis)
      .withQueryStringParameters(
        Param.Lat   -> latitude.toString,
        Param.Lon   -> longitude.toString,
        Param.AppID -> apiKey,
        Param.Units -> "metric",
      )
      .get()
      .map { response =>
        val results   = (response.json \ "list").as[List[JsValue]]

        results
          .map(_.as[WeatherInfo])
          .groupBy(_.date)
          .flatMap {
            case (_, weatherInfoList) => weatherInfoList.headOption
          }
          .toList
      }
  }
}

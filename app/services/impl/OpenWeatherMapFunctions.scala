package services.impl

import models.WeatherInfo

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
        val json = response.json
        ((json \ "lat").as[BigDecimal], (json \ "lon").as[BigDecimal])
      }
  }

  protected def getForecast(geoLocation: GeoLocation): Future[List[WeatherInfo]] = {
    geoLocation match {
      case (latitude, longitude) =>
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
            val results = response.json \\ "list"

            results.map(oneRecord => {
              val temp  = (oneRecord \ "main" \ "temp").as[BigDecimal]
              val dtTxt = (oneRecord \ "dt_txt").as[String].split(' ').head
              (temp, dtTxt)
            }).groupBy(_._2)
              .map { case (_, (temp, dtTxt) :: _) =>
                WeatherInfo.temp(temp, java.time.Instant.parse(dtTxt))
              }
              .toList
          }
    }
  }
}

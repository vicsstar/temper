package models

import org.joda.time.LocalDate
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, OFormat, Reads, __}

import java.time.Instant

case class WeatherInfo(
  temp:       BigDecimal,
  date:       Instant,
  location:   Option[String],
  created:    Option[Instant],
  id:         Option[Long],
) extends Product with Serializable

object WeatherInfo {

  implicit val jsonFormat: OFormat[WeatherInfo] = OFormat(
    {
      val tempReader  = (__ \ "main" \ "temp").read[BigDecimal]
      val dtTxtReader = (__ \ "dt_txt")
        .read[String]
        .map(_.split(' ').head)
        .map(LocalDate.parse)
        .map(_.toDate.toInstant)

      (tempReader and dtTxtReader)((temp, date) => WeatherInfo(temp, date, None, None, None))
    },
    Json.writes[WeatherInfo]
  )
}

package models

import java.time.Instant

case class WeatherInfo(
  temp:       BigDecimal,
  date:       Instant,
  location:   Option[String],
  created:    Option[Instant],
  id:         Option[Long],
)

object WeatherInfo {

  def temp(temp: BigDecimal, date: Instant) = WeatherInfo(temp, date, None, None, None)
}

package models

import java.time.Instant

case class WeatherInfo(
  temp:       BigDecimal,
  date:       Instant,
  location:   Option[String],
  created:    Option[Instant],
  id:         Option[Long],
) extends Product with Serializable

package utils

import db.{WeatherInfoSlickService, WeatherInfoTableManagement}
import models.WeatherInfo
import org.scalamock.scalatest.MockFactory

import java.time.Instant

trait MockHelpers {
  _: MockFactory =>

  val weatherInfoList = List(
    WeatherInfo(
      -2.3,
      Instant.now(),
      Some("espoo"),
      Some(Instant.now()),
      Some(1001),
    ),
    WeatherInfo(
      20.0,
      Instant.now(),
      Some("lagos"),
      Some(Instant.now()),
      Some(1003),
    )
  )

  class StubWeatherDbComponent extends WeatherInfoSlickService with WeatherInfoTableManagement {
    override lazy val db = ???
    override lazy val profile = ???
  }

  val stubWeatherDbComponent = mock[StubWeatherDbComponent]
}

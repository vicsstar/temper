package db

import slick.jdbc.JdbcBackend.Database

import javax.inject.{Inject, Singleton}

@Singleton
class WeatherInfoDatabaseComponent @Inject() (val db: Database)
  extends WeatherInfoSlickService with WeatherInfoTableManagement with DbProfile {
}

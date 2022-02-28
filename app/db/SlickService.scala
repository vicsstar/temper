package db

import models.WeatherInfo
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future

trait SlickService[T] {

  val db: Database

  type ID = Long

  def create(model: T): Future[ID]

  def delete(model: T): Future[Int]
}

trait WeatherInfoSlickService extends SlickService[WeatherInfo] {
  _: WeatherInfoTableManagement =>

  import profile.api._

  override def create(model: WeatherInfo): Future[ID] = db.run {
    (weatherInfo returning weatherInfo.map(_.id)) += model
  }

  override def delete(model: WeatherInfo): Future[Int] = ???
}

package db

import models.WeatherInfo
import slick.jdbc.JdbcBackend.Database

import java.time.{Duration, Instant}
import scala.concurrent.Future

trait SlickService[T] {

  val db: Database

  def create(model: T): Future[Int]

  def delete(model: T): Future[Int]
}

trait WeatherInfoSlickService extends SlickService[WeatherInfo] {
  _: WeatherInfoTableManagement =>

  import profile.api._

  override def create(model: WeatherInfo): Future[Int] = db.run {
    WeatherTable += model
  }

  override def delete(model: WeatherInfo): Future[Int] = db.run {
    WeatherTable.filter(_.id === model.id).delete
  }

  def findByTempForecast(days: Int): Future[Seq[WeatherInfo]] = db.run {
    val maxDate = Instant.now().plus(Duration.ofDays(days))
    WeatherTable.filter(_.date <= maxDate).sortBy(w => w.location -> w.date).result
  }
}

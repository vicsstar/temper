package db

import models.WeatherInfo
import slick.jdbc.JdbcProfile

import java.time.Instant

trait WeatherInfoTableManagement {

  val profile: JdbcProfile

  import profile.api._

  val WeatherTable = TableQuery[WeatherInfoTable]

  class WeatherInfoTable(tag: Tag) extends Table[WeatherInfo](tag, "weather_info") {
    def temp = column[BigDecimal]("temp")
    def date = column[Instant]("date")
    def location = column[String]("location")
    def created = column[Option[Instant]]("created")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * = (temp, date, location.?, created, id.?) <> ((WeatherInfo.apply _).tupled, WeatherInfo.unapply)
  }
}

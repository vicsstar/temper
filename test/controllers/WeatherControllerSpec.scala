package controllers

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import utils.MockHelpers

import scala.concurrent.{ExecutionContext, Future}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class WeatherControllerSpec extends PlaySpec
  with GuiceOneAppPerTest with MockFactory with MockHelpers {

  implicit lazy val ec: ExecutionContext = app.actorSystem.dispatcher

  "WeatherController GET /forecast" should {

    (stubWeatherDbComponent.findByTempForecast _)
      .expects(5)
      .returning(Future.successful(weatherInfoList))

    "render forecast content" in {
      val controller = new WeatherController(stubControllerComponents(), stubWeatherDbComponent)
      val forecastPage = controller.dayForecast(5).apply(FakeRequest(GET, "/forecast"))

      status(forecastPage) mustBe OK
      contentType(forecastPage) mustBe Some(JSON)
      contentAsJson(forecastPage) must be (Json.toJson(weatherInfoList))
    }
  }
}

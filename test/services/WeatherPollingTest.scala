package services

import akka.actor.ActorSystem
import akka.stream.scaladsl.Keep
import akka.stream.testkit.scaladsl.{TestSink, TestSource}
import db.WeatherInfoDatabaseComponent
import models.{LocationLimit, WeatherInfo}
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting
import utils.{MockHelpers, TestHelpers}

import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}

class WeatherPollingTest extends AnyFunSuite
  with GuiceOneAppPerTest with Injecting with MockFactory with MockHelpers with TestHelpers {
  thisSuite =>

  test("testGetWeatherInfoForSingleLocation") {
    val weatherPollingService = mock[WeatherPolling.Service]

    val weatherPolling = new WeatherPollingFunctions {
      override val pollingService: WeatherPolling.Service = weatherPollingService
      override lazy val weatherDbComponent: WeatherInfoDatabaseComponent = ???
      override implicit val ec: ExecutionContext = thisSuite.ec
    }

    (weatherPollingService.getWeatherInfo _)
      .expects("lagos")
      .returning(Future.successful(weatherInfoList.filter(_.location.contains("lagos"))))

    val flowUnderTest = weatherPolling.getWeatherInfoForSingleLocation

    val (pub, sub) = TestSource.probe[LocationLimit]
      .via(flowUnderTest)
      .toMat(TestSink())(Keep.both)
      .run()

    // downstream requests data
    sub.request(1)
    // upstream pushes data
    pub.sendNext(LocationLimit("lagos", 20.5))

    sub.expectNext(weatherInfoList.last -> LocationLimit("lagos", 20.5))

    succeed
  }

}

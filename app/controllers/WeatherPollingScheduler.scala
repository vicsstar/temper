package controllers

import akka.actor.{ActorRef, ActorSystem}
import org.slf4j.LoggerFactory
import play.api.inject.ApplicationLifecycle
import services.WeatherPolling.PollService

import javax.inject.{Inject, Named, Singleton}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WeatherPollingScheduler @Inject() (
                                          val actorSystem: ActorSystem,
                                          val appLifecycle: ApplicationLifecycle,
                                          @Named("weather-polling-service") val pollingActor: ActorRef,
                                        )(implicit val context: ExecutionContext) {

  val logger = LoggerFactory.getLogger(getClass)

  logger.info("Starting")

  appLifecycle.addStopHook(() => {
    logger.info("Shutting down weather service...")
    Future.successful(cancellable.cancel())
  })

  lazy val cancellable = actorSystem.scheduler.scheduleAtFixedRate(
    2.seconds,
    10.minutes,
    pollingActor,
    PollService
  )

  cancellable
}

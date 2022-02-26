import com.google.inject.AbstractModule
import controllers.WeatherPollingScheduler
import play.api.libs.concurrent.AkkaGuiceSupport
import services.WeatherPolling
import services.impl.OpenWeatherMapPollingService

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    bindActor[WeatherPolling]("weather-polling-service")
    bind(classOf[WeatherPolling.Service]).to(classOf[OpenWeatherMapPollingService])
    bind(classOf[WeatherPollingScheduler]).asEagerSingleton()
  }
}

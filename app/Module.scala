import com.google.inject.AbstractModule
import db.{DatabaseLifecycleManager, WeatherInfoDatabaseComponent, WeatherInfoSlickService}
import play.api.libs.concurrent.AkkaGuiceSupport
import services.impl.OpenWeatherMapPollingService
import services.{WeatherPolling, WeatherPollingScheduler}
import slick.jdbc.JdbcBackend.Database

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    bindActor[WeatherPolling]("weather-polling-service")
    bind(classOf[WeatherPolling.Service]).to(classOf[OpenWeatherMapPollingService])
    bind(classOf[WeatherPollingScheduler]).asEagerSingleton()

    bind(classOf[Database]).toInstance(Database.forConfig("temper"))
    bind(classOf[DatabaseLifecycleManager]).asEagerSingleton()

    bind(classOf[WeatherInfoSlickService]).to(classOf[WeatherInfoDatabaseComponent]).asEagerSingleton()
  }
}

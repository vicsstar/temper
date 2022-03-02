package db

import play.api.inject.ApplicationLifecycle
import slick.jdbc.JdbcBackend.Database

import javax.inject.{Inject, Singleton}

@Singleton
class DatabaseLifecycleManager @Inject()(appLifecycle: ApplicationLifecycle, db: Database) {

  appLifecycle.addStopHook(() => db.shutdown)
}

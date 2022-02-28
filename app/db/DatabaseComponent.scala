package db

import play.api.inject.ApplicationLifecycle
import slick.jdbc.JdbcBackend.Database

import javax.inject.{Inject, Singleton}

@Singleton
class DatabaseComponent @Inject()(appLifecycle: ApplicationLifecycle, db: Database) {

  appLifecycle.addStopHook(() => db.shutdown)
}

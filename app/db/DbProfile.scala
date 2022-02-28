package db

import slick.jdbc.PostgresProfile

trait DbProfile {
  _: SlickService[_] =>

  val profile = PostgresProfile
}

import sbt._

object Dependencies {

  val AkkaVersion       = "2.6.18"
  val SlickVersion      = "3.3.3"
  val FlywayPlayVersion = "7.18.0"
  val PostgresVersion   = "42.3.2"

  lazy val akka       = "com.typesafe.akka"   %% "akka-stream"  % AkkaVersion
  lazy val slick      = "com.typesafe.slick"  %% "slick"        % SlickVersion
  lazy val flywayPlay = "org.flywaydb"        %% "flyway-play"  % FlywayPlayVersion
  lazy val postgres   = "org.postgresql"       % "postgresql"   % PostgresVersion
}

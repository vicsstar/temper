import sbt._

object Dependencies {

  val AkkaVersion   = "2.6.18"
  val SlickVersion  = "3.3.3"

  lazy val akka   = "com.typesafe.akka"   %% "akka-stream"  % AkkaVersion
  lazy val slick  = "com.typesafe.slick"  %% "slick"        % SlickVersion
}

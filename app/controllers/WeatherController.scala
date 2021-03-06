package controllers

import db.WeatherInfoSlickService
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class WeatherController @Inject()(
                                   val controllerComponents: ControllerComponents,
                                   weatherDbComponent: WeatherInfoSlickService,
                                 )(implicit ec: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def dayForecast(days: Int) = Action.async { implicit request =>
    weatherDbComponent
      .findByTempForecast(days)
      .map(infoList =>
        Ok(Json.toJson(infoList))
      )
  }
}

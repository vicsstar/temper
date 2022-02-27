package utils

import play.api.Logger

trait HasLogger {

  protected val logger = Logger.apply(getClass)
}

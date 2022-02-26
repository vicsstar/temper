package object utils {

  implicit class AnyOps[T](obj: T) {

    def ? : Option[T] = Option(obj)
  }
}

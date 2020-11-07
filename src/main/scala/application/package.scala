package object application {
  def catchNonFatal[A](f: => A): Either[Throwable, A] =
    try {
      Right(f)
    } catch {
      case scala.util.control.NonFatal(t) => Left(t)
    }
}

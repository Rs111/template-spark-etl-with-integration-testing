package application.load

import application.model.TransformedData

trait Load {
  def write(transformedData: TransformedData): Either[Throwable, Unit]
}

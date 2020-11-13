package application

import application.extract.Extract
import application.transform.Transform
import application.load.Load

object RunETL extends ((Extract, Transform, Load) => Either[Throwable, Unit]) {
  def apply(extract: Extract, transform: Transform, load: Load): Either[Throwable, Unit] = {
    for {
      sourceData <- extract.read()
      transformedData <- transform.transform(sourceData)
      writeResult <-  load.write(transformedData)
    } yield {
      writeResult
    }
  }
}

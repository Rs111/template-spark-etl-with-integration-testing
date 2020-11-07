package application

import application.extract.Extract
import application.transform.Transform
import application.load.Load

object RunETL {
  def apply(extract: Extract, transform: Transform, load: Load): Either[Throwable, Unit] = {
    for {
      sourceData <- extract.read()
      transformedData <- transform.transform(sourceData)
      writeOutput <-  load.write(transformedData)
    } yield {
      writeOutput
    }
  }
}

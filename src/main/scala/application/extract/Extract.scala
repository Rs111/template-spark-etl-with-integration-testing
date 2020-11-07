package application.extract

import application.model.SourceData

trait Extract {
  def read(): Either[Throwable, SourceData]
}

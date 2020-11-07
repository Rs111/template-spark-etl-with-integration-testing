package application.transform

import application.model.{ SourceData, TransformedData}

trait Transform {
  def transform(sourceData: SourceData): Either[Throwable, TransformedData]
}

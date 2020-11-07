package application.transform

import application.model.{ SourceData, TransformedData }
import application.catchNonFatal
import org.apache.spark.sql.functions.{ col, count }

trait LiveTransform extends Transform {
  override def transform(source: SourceData): Either[Throwable, TransformedData] = {
    catchNonFatal(countRecordsByDate(source))
  }

  private def countRecordsByDate(sourceData: SourceData): TransformedData = {
    TransformedData(
      sourceData
        .df
        .groupBy(col("dt"))
        .agg(count("*").as("count"))
    )
  }
}

object LiveTransform {
  def apply(): LiveTransform = new LiveTransform {}
}

package application.load

import application.model.{ S3Path, TransformedData}
import application.catchNonFatal
import org.apache.spark.sql.SaveMode

trait LiveLoad extends Load {
  val outputPath: S3Path

  def write(transformedData:  TransformedData): Either[Throwable, Unit] = {
    catchNonFatal(writeAsParquet(transformedData))
  }

  private def writeAsParquet(transformedData: TransformedData): Unit = {
    transformedData
      .df
      .write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath.str)
  }
}

object LiveLoad {
  def apply(_outputPath: S3Path): LiveLoad = new LiveLoad {
    override val outputPath: S3Path = _outputPath
  }
}

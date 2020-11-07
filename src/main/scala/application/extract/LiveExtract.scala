package application.extract

import application.model.{ S3Path, SourceData }
import application.catchNonFatal
import org.apache.spark.sql.SparkSession

trait LiveExtract extends Extract {
  val spark: SparkSession
  val s3Path: S3Path

  override def read(): Either[Throwable, SourceData] = {
    catchNonFatal(readDataFrameFromParquet(s3Path))
  }

  private def readDataFrameFromParquet(s3Path: S3Path): SourceData = {
    SourceData(spark.read.format("parquet").load(s3Path.str))
  }
}

object LiveExtract {
  def apply(_s3Path: S3Path)(implicit _spark: SparkSession): LiveExtract = new LiveExtract {
    override val spark: SparkSession = _spark
    override val s3Path: S3Path = _s3Path
  }
}

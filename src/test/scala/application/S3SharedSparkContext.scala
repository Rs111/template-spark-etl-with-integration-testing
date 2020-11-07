package application

import application.S3Util.{ S3Port, createLocalServiceEndpoint }
import org.apache.hadoop.fs.s3a.{DefaultS3ClientFactory, S3AFileSystem}
import org.apache.spark.sql.SparkSession

trait S3SharedSparkContext {
  val spark: SparkSession
}

object S3SharedSparkContext {
  def apply(s3Port: S3Port): S3SharedSparkContext = new S3SharedSparkContext {
    override val spark: SparkSession = {
      val _spark =
        SparkSession
          .builder()
          .config("spark.hadoop.fs.s3a.impl", classOf[S3AFileSystem].getName)
          .config("spark.hadoop.fs.s3a.access.key", "foo")
          .config("spark.hadoop.fs.s3a.secret.key", "bar")
          .config("spark.hadoop.fs.s3a.path.style.access", "true")
          .config("spark.hadoop.fs.s3a.endpoint", createLocalServiceEndpoint(s3Port.int))
          .config("spark.hadoop.fs.s3a.attempts.maximum", "3")
          .config("spark.hadoop.fs.s3a.multiobjectdelete.enable", "false")
          .config("spark.hadoop.fs.s3a.change.detection.version.required", "false")
          .config("spark.hadoop.fs.s3a.committer.name", "directory")
          .config("spark.hadoop.fs.s3a.s3.client.factory.impl", classOf[DefaultS3ClientFactory].getName)
          .config("spark.sql.warehouse.dir", "s3a://spark-db/some_path")
          .master("local[2]")
          .appName("s3-spark-session")
          .getOrCreate()

    _spark.sparkContext.setLogLevel("ERROR")
    _spark
    }
  }
}

package application

import application.S3Util.{ S3Port, getS3BucketFromPath }
import application.DataFrameEqualityMatcher.equalTo
import application.extract.LiveExtract
import application.load.LiveLoad
import application.model.S3Path
import application.transform.LiveTransform
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RunETLSpec
  extends AnyFlatSpec
  with Matchers
  with RunningLocalS3WithClient
  with S3MockTargetComponent
  with S3ClientTargetComponent
  with SharedSparkContextTargetComponent
  with DataFixtures {

  private val s3Port = S3Port(9998)
  override val s3MockAdapter: S3MockTarget = S3MockTarget(s3Port)
  override val s3ClientAdapter: S3ClientTarget = S3ClientTarget(s3Port)
  override val sharedSparkContextAdapter: SharedSparkContextTarget = S3SharedSparkContextTarget(s3Port)
  private implicit val spark: SparkSession = sharedSparkContextAdapter.getSparkSession

  behavior of "RunETL"

  it should "successfully read, aggregate-by-date, and write data" in
    withS3Buckets(
      inputPath=S3Path("s3a://input-bucket/input-prefix"),
      outputPath=S3Path("s3a://output-bucket/output-prefix")
    ) { testPaths =>

      val testDataDF = createTestDataDF()
      testDataDF.write.parquet(testPaths.inputPath.str)

      RunETL(
        LiveExtract(testPaths.inputPath),
        LiveTransform(),
        LiveLoad(testPaths.outputPath)
      )

      val result = spark.read.parquet(testPaths.outputPath.str).orderBy(col("dt"))
      val expected = createExpectedDataDF()

      result should equalTo(expected)
  }

  case class TestPaths(inputPath: S3Path, outputPath: S3Path)

  private def withS3Buckets(inputPath: S3Path, outputPath: S3Path)(testCode: TestPaths => Any) = {
    val inputBucket = getS3BucketFromPath(inputPath)
    val outputBucket = getS3BucketFromPath(outputPath)

    try {
      s3ClientAdapter.createBucket(inputBucket)
      s3ClientAdapter.createBucket(outputBucket)
      testCode(TestPaths(inputPath, outputPath))
    }
    finally {
      s3ClientAdapter.deleteBucket(inputBucket)
      s3ClientAdapter.deleteBucket(outputBucket)
    }
  }
}

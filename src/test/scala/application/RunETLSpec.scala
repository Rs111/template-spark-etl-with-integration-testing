package application

import application.S3Util.{ S3Bucket, S3Port }
import application.DataFrameEqualityMatcher.equalTo
import application.extract.LiveExtract
import application.load.LiveLoad
import application.model.S3Path
import application.transform.LiveTransform
import org.apache.spark.sql.{ DataFrame, SparkSession }
import org.apache.spark.sql.functions.col
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RunETLSpec
  extends AnyFlatSpec
  with Matchers
  with RunningLocalSparkS3Environment
  with DataFixtures {

  private val s3Port = S3Port(9998)
  override val s3MockTarget: S3MockTarget = S3MockTarget(s3Port)
  override val s3ClientTarget: S3ClientTarget = S3ClientTarget(s3Port)
  override val s3SharedSparkContext: S3SharedSparkContext = S3SharedSparkContext(s3Port)
  private implicit val spark: SparkSession = s3SharedSparkContext.spark

  behavior of "RunETL"

  it should "successfully read, aggregate-by-date, and write data" in
    withTestDataInS3(
      S3Bucket("input-bucket"),
      S3Path("s3a://input-bucket/input-prefix"),
      S3Bucket("output-bucket"),
      S3Path("s3a://output-bucket/output-prefix"),
      createTestDataDF()
    ) { testInputs =>

      RunETL(
        LiveExtract(testInputs.inputPath),
        LiveTransform(),
        LiveLoad(testInputs.outputPath)
      )

      val result = spark.read.parquet(testInputs.outputPath.str).orderBy(col("dt"))
      val expected = createExpectedDataDF()

      result should equalTo(expected)
  }

  case class TestInputs(inputPath: S3Path, outputPath: S3Path, dataFixtures: DataFrame)

  private def withTestDataInS3(
    inputBucket: S3Bucket,
    inputPath: S3Path,
    outputBucket: S3Bucket,
    outputPath: S3Path,
    testDataDF: DataFrame)(testCode: TestInputs => Any) = {
    try {
      s3ClientTarget.amazonS3.createBucket(inputBucket.str)
      testDataDF.write.parquet(inputPath.str)
      s3ClientTarget.amazonS3.createBucket(outputBucket.str)
      testCode(TestInputs(inputPath, outputPath, testDataDF))
    }
    finally {
      s3ClientTarget.amazonS3.deleteBucket(inputBucket.str)
      s3ClientTarget.amazonS3.deleteBucket(outputBucket.str)
    }
  }
}

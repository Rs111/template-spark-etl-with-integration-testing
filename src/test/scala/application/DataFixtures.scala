package application

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{LongType, StringType, StructType}

trait DataFixtures {

  def createTestDataDF()(implicit spark: SparkSession): DataFrame = {
    val testData =
      Seq(
        Row("2020-11-01", "101"),
        Row("2020-11-01", "102"),
        Row("2020-11-02", "101"),
        Row("2020-11-03", "107"),
        Row("2020-11-03", "202"),
        Row("2020-11-03", "5000"),
        Row("2020-11-03", "5000")
      )

    val testDataSchema =
      new StructType()
        .add("dt", StringType, true)
        .add("txn_id", StringType, true)

    spark
      .createDataFrame(
        spark.sparkContext.parallelize(testData),
        testDataSchema
      )
  }


  def createExpectedDataDF()(implicit spark: SparkSession): DataFrame = {
    val expectedData =
      Seq(
        Row("2020-11-01", 2L),
        Row("2020-11-02", 1L),
        Row("2020-11-03", 4L)
      )

    val expectedDataSchema =
      new StructType()
        .add("dt", StringType, true)
        .add("count", LongType, true)

    spark
      .createDataFrame(
        spark.sparkContext.parallelize(expectedData),
        expectedDataSchema
      )
  }
}

import application.RunETL
import application.cli.ArgParser
import application.extract.LiveExtract
import application.load.LiveLoad
import application.transform.LiveTransform
import org.apache.spark.sql.SparkSession
import grizzled.slf4j.Logging

object Main extends App with ArgParser with Logging {

  implicit val spark: SparkSession = SparkSession.builder().getOrCreate()

  val maybeSuccess =
    for {
      cliArgs <- parseArgs(args)
      result <- RunETL(
        LiveExtract(cliArgs.inputPath),
        LiveTransform(),
        LiveLoad(cliArgs.outputPath)
      )
    } yield {
      result
    }

  maybeSuccess.fold(
    error => {
      logger.error("failure", error)
      throw new RuntimeException("etl failed")
    },
    _ => ()
  )

}

package application.cli

import application.model.S3Path

trait ArgParser {

  case class CliArgs(
                      inputPath: S3Path = S3Path(null),
                      outputPath: S3Path = S3Path(null)
                    )

  private val parser = new scopt.OptionParser[CliArgs]("scopt") {
    opt[String]("inputPath")
      .text("s3 source path")
      .required()
      .validate(x => validS3Path(x))
      .action((x, c) => c.copy(inputPath = S3Path(x)))

    opt[String]("outputPath")
      .text("s3 output path")
      .required()
      .validate(x => validS3Path(x))
      .action((x, c) => c.copy(outputPath = S3Path(x)))
  }

  def parseArgs(args: Array[String]): Either[Throwable, CliArgs] =
    parser
      .parse(args, CliArgs())
      .fold[Either[Throwable, CliArgs]](
        Left(new IllegalArgumentException(s"""Failure to parse args "${args.mkString}" """))
      )(c => Right(c))

  private def validS3Path(x: String): Either[String, Unit] = {
    if (x.split(":").head == "s3")
      Right(())
    else
      Left(s"""'$x' is not a valid s3 path""")
  }
}

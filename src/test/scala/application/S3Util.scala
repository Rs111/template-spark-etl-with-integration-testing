package application

import application.model.S3Path

object S3Util {
  case class S3Port(int: Int) extends AnyVal

  case class S3Protocol(str: String) extends AnyVal

  case class S3Bucket(str: String) extends AnyVal

  case class S3Prefix(str: String) extends AnyVal {
    def withSuffix(s: String): S3Prefix = S3Prefix(s"${this.str}/$s")
  }

  def createS3Path(
                    s3Protocol: S3Protocol,
                    s3Bucket: S3Bucket,
                    s3Prefix: S3Prefix
                  ): S3Path =
    S3Path(s"${s3Protocol.str}://${s3Bucket.str}/${s3Prefix.str}")

  def getS3BucketFromPath(s3Path: S3Path): S3Bucket = S3Bucket(s3Path.str.split("/")(2))

  def createLocalServiceEndpoint(port: Int): String = s"http://localhost:$port"
}

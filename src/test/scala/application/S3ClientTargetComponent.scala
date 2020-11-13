package application

import application.S3Util.{S3Bucket, S3Port, createLocalServiceEndpoint}
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

trait S3ClientTargetComponent {
  val s3ClientAdapter: S3ClientTarget
}

trait S3ClientTarget {
  val amazonS3: AmazonS3

  def createBucket(s3Bucket: S3Bucket): Unit = amazonS3.createBucket(s3Bucket.str)
  def deleteBucket(s3Bucket: S3Bucket): Unit = amazonS3.deleteBucket(s3Bucket.str)
  def shutdown(): Unit = amazonS3.shutdown()
}

object S3ClientTarget {
  def apply(s3Port: S3Port): S3ClientTarget = new S3ClientTarget {
    override val amazonS3: AmazonS3 =
      AmazonS3ClientBuilder
        .standard
        .withPathStyleAccessEnabled(true)
        .disableChunkedEncoding
        .withEndpointConfiguration(
          new AwsClientBuilder.EndpointConfiguration(
            createLocalServiceEndpoint(s3Port.int),
            Regions.US_EAST_1.getName
          )
        )
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar")))
        .build()
  }
}



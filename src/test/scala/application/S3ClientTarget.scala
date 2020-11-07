package application

import application.S3Util.{ S3Port, createLocalServiceEndpoint }
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

trait S3ClientTarget {
  val amazonS3: AmazonS3
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

package application

import application.S3Util.S3Port
import io.findify.s3mock.S3Mock

trait S3MockTargetComponent {
  val s3MockAdapter: S3MockTarget
}

trait S3MockTarget {
  val s3Mock: S3Mock

  def start(): Unit = s3Mock.start
  def shutdown(): Unit = s3Mock.shutdown
}

object S3MockTarget {
  def apply(s3Port: S3Port): S3MockTarget = new S3MockTarget {
    override val s3Mock: S3Mock = new S3Mock.Builder().withPort(s3Port.int).withInMemoryBackend.build
  }
}

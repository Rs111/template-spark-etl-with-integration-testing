package application

import org.scalatest.{ BeforeAndAfterAll, Suite }

trait RunningLocalSparkS3Environment extends BeforeAndAfterAll { this: Suite =>
  val s3MockTarget: S3MockTarget
  val s3ClientTarget: S3ClientTarget
  val s3SharedSparkContext: S3SharedSparkContext

  override def beforeAll(): Unit = {
    super.beforeAll()
    s3MockTarget.s3Mock.start
  }

  override def afterAll(): Unit = {
    s3ClientTarget.amazonS3.shutdown()
    s3MockTarget.s3Mock.shutdown
    super.afterAll()
  }
}

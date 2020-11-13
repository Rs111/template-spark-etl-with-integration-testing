package application

import org.scalatest.{BeforeAndAfterAll, Suite}

trait RunningLocalS3EnvironmentWithClient extends BeforeAndAfterAll {
  this: Suite
    with S3MockTargetComponent
    with S3ClientTargetComponent =>

  override def beforeAll(): Unit = {
    super.beforeAll()
    s3MockAdapter.start()
  }

  override def afterAll(): Unit = {
    s3ClientAdapter.shutdown()
    s3MockAdapter.shutdown()
    super.afterAll()
  }
}



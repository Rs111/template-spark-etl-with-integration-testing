## template-spark-etl-with-integration-testing 

**Purpose**
* To create a simple Spark/S3 ETL and integration test it locally
* The takeways are how to:
    * Set-up, configure, and use local Spark, S3Mock, and S3Client to integration test locally
    * Leverage design patterns and principals (e.g. adapter pattern, cake pattern, dependency inversion) to write a clean and testable ETL
* Note: the application is bare-bones in functionality (it just reads some data, does a group by, and writes to S3)

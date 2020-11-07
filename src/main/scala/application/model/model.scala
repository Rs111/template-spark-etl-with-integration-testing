package application

import org.apache.spark.sql.DataFrame

package object model {

  case class S3Path(str: String) extends AnyVal {
    def withSuffix(str: String): S3Path = S3Path(s"${this.str}/$str")
  }

  case class SourceData(df: DataFrame) extends AnyVal

  case class TransformedData(df: DataFrame) extends AnyVal

}

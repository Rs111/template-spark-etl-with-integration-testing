package application

import org.apache.spark.sql.DataFrame
import org.scalatest.matchers.{ MatchResult, Matcher }

class DataFrameEqualityMatcher(right: DataFrame) extends Matcher[DataFrame] {
  override def apply(left: DataFrame): MatchResult =
    if (!left.schema.equals(right.schema)) {
      MatchResult(
        false,
        s"Schema ${left.schema} did not match expected schema ${right.schema}",
        ""
      )
    }
    else if (!left.collect().sameElements(right.collect())) {
      MatchResult(
        false,
        s"Dataframe content ${left.collectAsList()} did not match expected content ${right.collectAsList()}",
        ""
      )
    }
    else {
      MatchResult(
        true,
        "",
        s"Dataframe had schema ${left.schema} and content ${left.collectAsList()}"
      )
    }
}

object DataFrameEqualityMatcher {
  def equalTo(df: DataFrame): DataFrameEqualityMatcher = new DataFrameEqualityMatcher(df)
}


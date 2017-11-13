package com.powerspace.pg2bq
import org.apache.spark.sql.DataFrame

trait DataExporter {
  def read(tableName: String): DataFrame
}

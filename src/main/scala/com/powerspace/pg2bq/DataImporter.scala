package com.powerspace.pg2bq

import org.apache.spark.sql.DataFrame

trait DataImporter {
  def createOrOverride(df: DataFrame, tableName: String): Unit
}

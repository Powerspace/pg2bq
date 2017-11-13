package com.powerspace.pg2bq

import java.util.Properties

import com.powerspace.pg2bq.config.JdbcConfiguration
import org.apache.spark.sql.{DataFrame, SparkSession}

class PostgreSqlExporter(spark: SparkSession, jdbc: JdbcConfiguration) extends DataExporter {

  val props = new Properties()
  props.put("user", jdbc.user)
  props.put("password", jdbc.password)
  props.put("driver", "org.postgresql.Driver")

  override def read(tableName: String): DataFrame = {
    spark.read.jdbc(jdbc.url, tableName, props)
  }
}

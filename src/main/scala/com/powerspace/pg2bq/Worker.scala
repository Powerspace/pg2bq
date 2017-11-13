package com.powerspace.pg2bq

import com.powerspace.pg2bq.config.ApplicationConfig
import org.apache.spark.sql.SparkSession

object Worker {
  def start(config: ApplicationConfig) = {
    implicit val spark = SparkSession
      .builder()
      .appName("JDBC to BigQuery exporter")
      .master("local[*]")
      .config("google.cloud.auth.service.account.json.keyfile", config.gcloud.serviceAccountKeyPath)
      .config("fs.gs.project.id", config.gcloud.project)
      .getOrCreate()

    val jdbc = new PostgreSqlExporter(spark, config.jdbc)
    val bq = new BigQueryImporter(spark, config.gcloud.gcs.tmpBucket, config.gcloud.bq.dataset)

    config.jdbc.tables.foreach { table =>
      val df = jdbc.read(table)
      bq.createOrOverride(df, table)
    }
  }
}

package com.powerspace.pg2bq

import com.google.cloud.bigquery.JobInfo.WriteDisposition
import com.google.cloud.bigquery._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class BigQueryImporter(spark: SparkSession, tmpBucket: String, dataset: String) extends LazyLogging with DataImporter {

  val bigquery: BigQuery = BigQueryOptions.getDefaultInstance.getService

  // ensure the dataset exists or create it
  getOrCreateDataset(dataset)

  override def createOrOverride(df: DataFrame, tableName: String): Unit = {
    saveIntoGcs(df, tableName)
    loadFromGcsToBq(tableName)
  }

  private def loadFromGcsToBq(tableName: String): Unit = {
    val configuration = LoadJobConfiguration
      .builder(TableId.of(dataset, tableName), s"gs://$tmpBucket/$tableName/*.avro")
      .setFormatOptions(FormatOptions.avro())
      .setWriteDisposition(WriteDisposition.WRITE_TRUNCATE)
      .build()

    val job = bigquery.create(JobInfo.newBuilder(configuration).build())

    logger.info(s"Importing $tableName from bucket $tmpBucket to dataset $dataset...")
    job.waitFor()
    logger.info(s"$tableName import done!")
  }

  private def saveIntoGcs(df: DataFrame, tableName: String): Unit = {
    df.write
      .mode(SaveMode.Overwrite)
      .format("com.databricks.spark.avro")
      .save(s"gs://$tmpBucket/$tableName")
  }

  def getOrCreateDataset(datasetName: String): Dataset = {
    scala.Option(bigquery.getDataset(datasetName)) match {
      case Some(ds) =>
        logger.info(s"Dataset $datasetName already exist.")
        ds
      case None =>
        logger.info(s"Dataset $datasetName does not exist, creating...")
        val ds = bigquery.create(DatasetInfo.of(datasetName))
        logger.info(s"Dataset $datasetName created!")
        ds
    }
  }

}

package com.powerspace.pg2bq.config

case class GCloudConfiguration(
    project: String,
    serviceAccountKeyPath: String,
    bq: BigQueryConfiguration,
    gcs: GcsConfiguration
)

case class BigQueryConfiguration(
    dataset: String
)

case class GcsConfiguration(
    tmpBucket: String
)

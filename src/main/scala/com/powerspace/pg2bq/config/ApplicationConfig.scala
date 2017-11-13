package com.powerspace.pg2bq.config

case class ApplicationConfig(
    jdbc: JdbcConfiguration,
    gcloud: GCloudConfiguration
)

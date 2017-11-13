package com.powerspace.pg2bq

import com.powerspace.pg2bq.config.ApplicationConfig
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import pureconfig.error.ConfigReaderFailures
import pureconfig.loadConfig

object Main extends App with LazyLogging {

  loadConfig[ApplicationConfig](ConfigFactory.load()) match {
    case Left(failures: ConfigReaderFailures) =>
      logger.error(s"Incorrect configuration: ${failures.toList.mkString(", ")}")
    case Right(config) =>
      Worker.start(config)
  }

}

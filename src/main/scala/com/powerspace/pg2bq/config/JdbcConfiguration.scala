package com.powerspace.pg2bq.config

case class JdbcConfiguration(
    url: String,
    user: String,
    password: String,
    tables: List[String]
)

# pg2bq

pg2bq is made for one thing: export tables from PostgreSQL to Google BigQuery.

# Why ?

It's useful to keep the data at both places "in-sync" (using cron, airflow, or whatever to schedule the export on a regular basis).
If your metadata are on PostgreSQL, but your _realtime_ data are in BigQuery, it's probable you want to join them.

> Note: internally, it is using the Spark framework for the sake of simplicity, but no Hadoop cluster is needed. It is configured as a "local" cluster by default, meaning the application is running standalone.

# How to run it

- Download the release made on GitHub: [pg2bq-1.0.3.zip](https://github.com/Powerspace/pg2bq/releases/download/v1.0.3/pg2bq-1.0.3.zip)
- Create a Service Account to Google Cloud which has access to GCS and BigQuery, and create a json key
- Create a configuration file `configuration.conf` for pg2bq to know where to grab and put the data (HOCON):
```
jdbc {
  url = "jdbc:postgresql://mypg:5432/mydb"
  user = "myuser"
  password = "mypwd"
  tables = [ "user", "campaign", "website" ]
}

gcloud {
  project = "gcloud-project-id"
  service-account-key-path = "/path/to/service-account-key.json"
  bq.dataset = "mypg"
  gcs.tmp-bucket = "pg-export-tmp"
}
```
- Run the application specifying the config file:
```
GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account-key.json ./bin/pg2db -Dconfig.file=configuration.conf
```
- Done!

Add this to a scheduler every 10min and enjoy your JOINs in BigQuery.

# What does it do exactly ?

- It exports the data from the tables into DataFrames
- It saves them into GCS as `.avro` to keep the schema along the data: this will avoid to specify/create the BigQuery table schema beforehands.
- It starts BigQuery jobs to import those `.avro` into the respective BigQuery tables.

# Development

To run the application in dev mode, create a proper `application.conf` and run the app:

- via the IDE, just run `Main`
- via sbt, by pre-packaging the whole thing:
```
$ sbt stage && ./target/universal/stage/bin/pg2bq
```

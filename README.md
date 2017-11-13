# pg2bq

pg2bq is made for one thing: export tables from PostgreSQL to Google BigQuery.

It's useful to keep the data at both places "in-sync" (by using cron, airflow, or whatever to schedule the export on a regular basis).

Note: internally, it is using the Spark framework for the sake of simplicity, but no Hadoop cluster is needed. It is configured as a "local" cluster by default, meaning the application is running standalone.

# How to run it

- Download the release made on GitHub: ...
- Create a Service Account to Google Cloud which has access to GCS and BigQuery, and create a json key
- Create a configuration file for pg2bq to know where to grab and put the data:
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
- Run the application:
```
./bin/pg2db -Dconfig.file=configuration.json
```
- Done!

# Development

To run the application in dev mode, set a proper `application.conf` and run the app:

- via the IDE, just run `Main`
- via sbt, by pre-packaging the whole thing:
````
$ sbt stage && ./target/universal/stage/bin/pg2bq
```
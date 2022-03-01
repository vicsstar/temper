# Temper - Weather Service

### Setup

- Download and install docker from https://www.docker.com/
- Download and install scala and sbt respectively, from:
  - https://scala.org
  - https://www.scala-sbt.org

This program was tested on systems running Java 8 and 11.

### Running the database service
Create a `.env` file in the project's root directory, containing the following keys for database access:

```
DB_USER=username    # username to log in to the database.
DB_PASSWORD=password    # password to log in to the database.
```

Access the project using a terminal. In the project's root directory, start the Postgres database service using docker:

`docker compose up`

or with a single command at the terminal, with the relevant environment variables:

`DB_USER=username DB_PASSWORD=password docker compose up`

This database service should also be accessible on the host machine (127.0.0.1), on port 5431
_(to prevent clash with any existing database service running on the standard 5432 port)_.

### Running the tests
Simply execute at the terminal, the command: `sbt test`.

_Note: the application uses Flyway migrations so, you should have a valid database instance running to successfully run the tests._

### Accessing the service
To start the service, run `sbt runProd`, to run it in production mode, so the polling service start automatically. 

When the service starts successfully, there is a single endpoint that exposes the forecast data stored in the database.

It should be accessible on your local machine at: http://127.0.0.1:9000/forecast

In the case there is a clash with another port on your local machine, you can start the service on a port of your choice:

`sbt "runProd <port>"`

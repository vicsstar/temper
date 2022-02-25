# Temper - Weather Service

### Setup

- Download and install docker desktop from https://www.docker.com/
- Download and install scala and sbt respectively, from:
  - https://scala.org
  - https://www.scala-sbt.org

### Running the service
Create a `.env` file in the project's root directory, containing the following keys for database access:

```
DB_USER=username    # username to log in to the database.
PASSWORD=password    # password to log in to the database.
```

Access the project using a terminal. In the project's root directory, start the Postgres database service using docker:

`docker compose up`

This database service should also be accessible on the host machine (127.0.0.1), on port 5431
_(to prevent clash with any existing database service running on the standard 5432 port)_.
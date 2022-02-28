-- ! Downs
DROP TABLE IF EXISTS weather_info;

-- ! Ups
CREATE TABLE weather_info(
    "id"        BIGSERIAL PRIMARY KEY,
    "date"      TIMESTAMP NOT NULL,
    "temp"      NUMERIC NOT NULL,
    "location"  TEXT NOT NULL,
    "created"   TIMESTAMP DEFAULT now(),
    UNIQUE (location, date)
);

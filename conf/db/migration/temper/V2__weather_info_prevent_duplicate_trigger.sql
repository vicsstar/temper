DROP FUNCTION IF EXISTS fn_prevent_duplicate_in_weather_info;

CREATE FUNCTION fn_prevent_duplicate_in_weather_info() RETURNS trigger
AS $$
DECLARE
    existing_record RECORD;
BEGIN
    select * from "weather_info" where "location" = NEW."location" AND "date" = NEW."date" into existing_record;

    IF exists (select * from "weather_info" where "location" = NEW."location" AND "date" = NEW."date") THEN
        RETURN NULL;
    ELSE
        RETURN NEW;
    END IF;
END; $$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg__weather_info__prevent_duplicate ON weather_info;

CREATE TRIGGER trg__weather_info__prevent_duplicate
    BEFORE INSERT ON weather_info
    FOR EACH ROW
    EXECUTE PROCEDURE fn_prevent_duplicate_in_weather_info();

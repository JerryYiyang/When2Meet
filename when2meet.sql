USE when2meet;

CREATE TABLE event_table(
	eid VARCHAR(128) UNIQUE,
    title VARCHAR(128)
);

CREATE TABLE availability(
	session_id SERIAL UNIQUE,
	user_name VARCHAR(128),
    date VARCHAR(128),
    time VARCHAR(128),
    eid INT REFERENCES event_table(eid)
);

SELECT * FROM event_table;
SELECT * FROM availability;

# Get all the names
SELECT title, user_name, date, time
FROM event_table INNER JOIN availability;

USE when2meet;

CREATE TABLE e_date(
	date_id SERIAL PRIMARY KEY,
    date_time VARCHAR(128)
);

CREATE TABLE event_table(
	eid VARCHAR(128) PRIMARY KEY,
    e_name VARCHAR(128)
);

CREATE TABLE person(
	pid SERIAL PRIMARY KEY,
    username VARCHAR(128)
);

CREATE TABLE availability(
	start_time VARCHAR(128),
    end_time VARCHAR(128),
    p_name VARCHAR(128) REFERENCES person(username),
    event_date VARCHAR(128) REFERENCES e_date(date_time),
    event_id VARCHAR(128) REFERENCES event_table(eid)
);

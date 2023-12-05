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
    event_id VARCHAR(128) REFERENCES event_table(eid),
    PRIMARY KEY(p_name, event_id);
);

-- AR: Return the date and time form the given eid
SELECT start_time, end_time, eid
FROM event_table e
INNER JOIN availability a ON e.edi = a.event_id;

-- AR: create statement to create new events
INSERT INTO e_date() VALUES(date_time VARCHAR(128));
INSERT INTO event_table() VALUES(eid VARCHAR(128), e_name VARCHAR(128));
INSERT INTO person VALUES(username VARCHAR(128));
INSERT INTO availability VALUES(start_time VARCHAR(128), end_time VARCHAR(128), p_name VARCHAR(128), 
								event_date VARCHAR(128), event_id VARCHAR(128));
                                
-- AR: edit and remove time slots
-- Remove time slot
UPDATE availability SET start_time = ...;
UPDATE availability SET end_time = ...;

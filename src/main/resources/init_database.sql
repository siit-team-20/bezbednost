CREATE DATABASE booking_baboon_database;

\c booking_baboon_database;

CREATE USER booking_baboons WITH PASSWORD 'm0nk3';

GRANT ALL PRIVILEGES ON DATABASE booking_baboon_database TO booking_baboons;

ALTER DATABASE booking_baboon_database OWNER TO booking_baboons;
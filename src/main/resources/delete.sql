SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'booking_baboon_database' AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS booking_baboon_database;



SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.usename = 'booking_baboons' AND pid <> pg_backend_pid();

DROP USER IF EXISTS booking_baboons;

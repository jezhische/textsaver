CREATE DATABASE textsaver;
CREATE USER texter WITH password 'password';
GRANT ALL ON DATABASE textsaver TO texter;

-- working commands, but do not any effect:
SET search_path TO postgres;
SET SEARCH_PATH = textsaver;

ALTER ROLE postgres WITH PASSWORD 'password'; -- if the role is not 'postgres', throws ERROR: must be superuser to
-- alter superusers
-- ****************
SET ROLE postgres; -- this command works for any current role, and after it 'alter role' works too
-- ****************
GRANT ALL ON DATABASE postgres TO postgres;
SET ROLE texter;


DROP DATABASE IF EXISTS textsaver; -- works only if the current role is postgres (superuser)

CREATE SCHEMA IF NOT EXISTS examples;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA examples TO postgres;
DROP SCHEMA IF EXISTS examples;



-- ******************************************************************
CREATE DATABASE examples;
-- CREATE USER texter WITH password 'password';
GRANT ALL ON DATABASE examples TO postgres WITH GRANT OPTION;


CREATE SCHEMA IF NOT EXISTS textsaver;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA textsaver TO postgres; -- qualified name consisting of the database name and schema name

DROP SCHEMA IF EXISTS textsaver;

-- qualified name consisting of the schema name and table name


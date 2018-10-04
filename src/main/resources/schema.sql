-- CREATE DATABASE textsaver
-- WITH OWNER = postgres
-- ENCODING = 'UTF8'
-- TABLESPACE = pg_default
-- LC_COLLATE = 'Russian_Russia.1251'
-- LC_CTYPE = 'Russian_Russia.1251'
-- CONNECTION LIMIT = -1;
-- GRANT CONNECT, TEMPORARY ON DATABASE textsaver TO public;
-- GRANT ALL ON DATABASE textsaver TO postgres WITH GRANT OPTION;

CREATE TABLE IF NOT EXISTS public.text_common_data (
    id SERIAL PRIMARY KEY
  , name VARCHAR(255)
  , creating_date TIMESTAMP without time zone DEFAULT now()
  , updating_date TIMESTAMP without time zone DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.text_parts (
  id SERIAL PRIMARY KEY
  , body TEXT
  , previous_item BIGINT UNIQUE
  , next_item BIGINT UNIQUE
  , text_common_data_id BIGINT
  , last_update TIMESTAMP without time zone DEFAULT now()
-- FOREIGN KEY (text_common_data_id) - ссылка на колонку в этой таблице
  , CONSTRAINT fk_textParts_textCommonData FOREIGN KEY (text_common_data_id) REFERENCES text_common_data (id)
);

CREATE INDEX IF NOT EXISTS idx_next_it
  ON public.text_parts
  USING hash
  (next_item);

CREATE INDEX IF NOT EXISTS idx_prev_it
  ON public.text_parts
  USING hash
  (previous_item);





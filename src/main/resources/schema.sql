-- CREATE DATABASE textsaver
-- WITH OWNER = postgres
-- ENCODING = 'UTF8'
-- TABLESPACE = pg_default
-- LC_COLLATE = 'Russian_Russia.1251'
-- LC_CTYPE = 'Russian_Russia.1251'
-- CONNECTION LIMIT = -1;
-- GRANT CONNECT, TEMPORARY ON DATABASE textsaver TO public;
-- GRANT ALL ON DATABASE textsaver TO postgres WITH GRANT OPTION;

-- DROP DATABASE IF EXISTS textsaver; -- works only if the current role is postgres (superuser)


-- DROP SEQUENCE public.hibernate_sequence;

CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence;
ALTER TABLE public.hibernate_sequence
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS public.text_common_data (
    id BIGINT NOT NULL
  , name VARCHAR(255)
  , first_item BIGINT
  , creating_date TIMESTAMP without time zone DEFAULT now()
  , updating_date TIMESTAMP without time zone DEFAULT now()
  , CONSTRAINT text_common_data_pkey PRIMARY KEY (id)
);

-- DROP TABLE IF EXISTS public.text_common_data CASCADE;

CREATE TABLE IF NOT EXISTS public.text_parts (
  id BIGINT NOT NULL
  , body TEXT
  , next_item BIGINT UNIQUE
  , text_common_data_id BIGINT
  , last_update TIMESTAMP without time zone DEFAULT now()
  , CONSTRAINT text_parts_pkey PRIMARY KEY (id)
-- FOREIGN KEY (text_common_data_id) - ссылка на колонку в этой таблице
  , CONSTRAINT fk_textParts_textCommonData FOREIGN KEY (text_common_data_id) REFERENCES text_common_data (id)
--     ON UPDATE CASCADE ON DELETE CASCADE -- default NO ACTION
);

CREATE INDEX IF NOT EXISTS idx_next_it
  ON public.text_parts
  USING hash
  (next_item);

-- DROP TABLE IF EXISTS public.text_parts CASCADE;



-- CREATE TYPE bookmark_def AS (
--   page_number INTEGER,
--   is_edited BOOLEAN
-- );

-- DROP TYPE bookmark_def CASCADE ;

-- CREATE TABLE IF NOT EXISTS bookmark_def (
--   page_number INTEGER,
--   is_edited BOOLEAN
-- );

-- DROP TABLE bookmark_def CASCADE ;


CREATE TABLE IF NOT EXISTS public.bookmarks
(
  text_common_data_id BIGINT NOT NULL,
  last_open_array text [],
  special_bookmarks integer[],
--   NB: PK and FK are the same
  CONSTRAINT bookmarks_pkey PRIMARY KEY (text_common_data_id),
  CONSTRAINT fk_bookmarks_textCommonData FOREIGN KEY (text_common_data_id)
  REFERENCES public.text_common_data (id)
);

-- DROP TABLE IF EXISTS public.bookmarks CASCADE;

-- INSERT INTO bookmarks VALUES (13, '{}');

CREATE OR REPLACE FUNCTION get_remaining_texparts_ordered_set(IN this_text_part_id BIGINT) RETURNS SETOF public.text_parts AS
'
DECLARE
  r public.text_parts%ROWTYPE;
  next_id BIGINT;
  this_text_common_data_id BIGINT;
BEGIN
  r := (SELECT tp FROM public.text_parts AS tp WHERE tp.id = this_text_part_id);
--   there is need in explicit casting in non-SQL expressions, so r casts to text_parts type
  this_text_common_data_id := (r::public.text_parts).text_common_data_id;
  next_id := (r::public.text_parts).next_item;
  RETURN NEXT r;
  IF (next_id IS NULL) THEN
    RETURN;
  END IF;
  FOR r IN SELECT * FROM public.text_parts AS tp WHERE tp.text_common_data_id = this_text_common_data_id
  LOOP
    r := (SELECT tp FROM public.text_parts AS tp WHERE tp.id = next_id);
    next_id := (r::text_parts).next_item;
    --   Since the search runs through the whole set of text_part, when it encounters r.nextItem = null, then the function
    --   returns next r = null. To avoid such result:
    IF ((r::public.text_parts).id IS NULL) THEN
      CONTINUE;
    END IF;
    RETURN NEXT r;
  END LOOP;
END
'
LANGUAGE plpgsql;

-- SELECT * FROM get_remaining_texparts_ordered_set(62);


CREATE OR REPLACE FUNCTION get_all_texparts_ordered_set(IN this_text_common_data_id BIGINT) RETURNS SETOF public.text_parts AS
-- NB: function body must be something like "one statement" for JPA, so the "dollars quotes" $$ there are
-- no suitable here, only '. But they are suitable INTO the function body
'
DECLARE
  r public.text_parts%ROWTYPE;
  next_id BIGINT;
BEGIN
  r := (SELECT tp FROM public.text_parts AS tp WHERE tp.id =
  (SELECT tcd.first_item FROM text_common_data AS tcd WHERE tcd.id = this_text_common_data_id));
  next_id := (r::text_parts).next_item;
  RETURN NEXT r;
  FOR r IN SELECT * FROM public.text_parts AS tp WHERE tp.text_common_data_id = this_text_common_data_id   -- попробовать здесь функцию get_textpart_by_id
  LOOP
--     RAISE NOTICE $$current r.next_item: %, r.id: %$$, (r::text_parts).next_item, (r::text_parts).id;
    r := (SELECT tp FROM public.text_parts AS tp WHERE tp.id = next_id);
    --     casting to text_parts to specify type explicitly to get field next_item from r composite type
    next_id := (r::text_parts).next_item;
--   Since the search runs through the whole set of text_part, when it encounters r.nextItem = null, then the function
--   returns next r = null. To avoid such result:
    IF ((r::public.text_parts).id IS NULL) THEN
      CONTINUE;
    END IF;
    RETURN NEXT r;
  END LOOP;
  RETURN;
END
'
LANGUAGE plpgsql;


-- SELECT * FROM get_all_texparts_ordered_set(36);


CREATE OR REPLACE FUNCTION get_all_texparts_id_ordered_set(IN this_text_common_data_id BIGINT) RETURNS SETOF BIGINT AS
'
DECLARE
  r public.text_parts%ROWTYPE;
  next_id BIGINT;
BEGIN
  r := (SELECT tp FROM public.text_parts AS tp WHERE tp.id =
  (SELECT tcd.first_item FROM text_common_data AS tcd WHERE tcd.id = this_text_common_data_id));
  next_id := (r::text_parts).next_item;
  RETURN NEXT (r::text_parts).id;
  FOR r IN SELECT * FROM public.text_parts AS tp WHERE tp.text_common_data_id = this_text_common_data_id   -- попробовать здесь функцию get_textpart_by_id
  LOOP
    r := (SELECT tp FROM public.text_parts AS tp WHERE tp.id = next_id);
    --     casting to text_parts to specify type explicitly to get field next_item from r composite type
    next_id := (r::text_parts).next_item;
--   Since the search runs through the whole set of text_part, when it encounters r.nextItem = null, then the function
--   returns next r = null. To avoid such result:
    IF ((r::public.text_parts).id IS NULL) THEN
      CONTINUE;
    END IF;
    RETURN NEXT (r::text_parts).id;
  END LOOP;
  RETURN;
END
'
LANGUAGE plpgsql;

-- SELECT * FROM get_all_texparts_id_ordered_set(36);


-- NB: BIGINT for Long type variable
CREATE OR REPLACE FUNCTION get_textparts_ordered_set(IN start_id BIGINT, IN size INTEGER) RETURNS SETOF public.text_parts AS
-- NB: function body must be something like "one statement" for JPA, so the "dollars quotes" $$ there are no suitable here, only '
'
DECLARE
  r public.text_parts%ROWTYPE;
  next_id BIGINT;
BEGIN
  next_id := start_id;
  FOR i IN 1..$2  -- NB: two dots, not three
  LOOP
--   The function keeps order where it searches the next r by the properties of previous $2 times. So if r.next_item = null,
--   it is the last element in list.
    IF (next_id IS NULL) THEN
      RETURN;
    END IF ;
    r := (SELECT tp FROM public.text_parts AS tp WHERE id = next_id);
    --     casting to text_parts to specify type explicitly to get field next_item from r composite type
    next_id := (r::text_parts).next_item;
--     RAISE NOTICE $$current r: %$$, r;
    RETURN NEXT r;
  END LOOP;
  RETURN;
END;
'
LANGUAGE plpgsql;

-- SELECT * FROM get_textparts_ordered_set(52, 10);


CREATE OR REPLACE FUNCTION find_textpart_by_id(IN tpid BIGINT, OUT textpart text_parts) AS
'
BEGIN
  RAISE NOTICE $$id = %$$, tpid;
  SELECT * INTO textpart FROM text_parts AS tp WHERE tp.id = tpid;
END;
'
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_textcommondata_by_id(tcdid BIGINT, OUT textcommondata text_common_data) AS
'
BEGIN
  RAISE NOTICE $n$id = %$n$, tcdid;
  SELECT * INTO textcommondata FROM public.text_common_data AS tcd WHERE tcd.id = tcdid;
END
'
LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION update_text_part_by_id(tpid BIGINT, updated_body TEXT, updated TIMESTAMP) RETURNS TIMESTAMP AS
'
BEGIN
  UPDATE public.text_parts AS tp
    SET body = updated_body,
      last_update = updated
  WHERE tp.id = tpid;
--   RETURN updated;
  RETURN (SELECT tp.last_update FROM public.text_parts AS tp WHERE tp.id = tpid);
END
'
  LANGUAGE plpgsql;

-- SELECT * FROM update_text_part_by_id(44, 'new body: 44 next 46');

-- CREATE OR REPLACE FUNCTION update_text_part_by_id(tpid BIGINT, updated_body TEXT) RETURNS TIMESTAMP AS
-- '
-- BEGIN
--   UPDATE public.text_parts AS tp
--     SET body = updated_body
--   WHERE tp.id = tpid;
-- --   RETURN updated;
--   RETURN (SELECT tp.last_update FROM public.text_parts AS tp WHERE tp.id = tpid);
-- END
-- '
-- LANGUAGE plpgsql;
--
-- SELECT * FROM public.update_text_part_by_id(44, 'new body - updated: 44 next 46');

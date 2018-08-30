CREATE TABLE IF NOT EXISTS text_parts (
  id SERIAL PRIMARY KEY,
  body TEXT
);

-- ****************************************************** from pgAdmin:
CREATE TABLE public.text_parts
(
  id integer NOT NULL DEFAULT nextval('text_parts_id_seq'::regclass),
  body text,
  CONSTRAINT text_parts_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE public.text_parts
  OWNER TO postgres;

-- *****************************************************************
-- CREATE TABLE IF NOT EXISTS text_parts (
--   id INT NOT NULL,
--   body TEXT,
--   PRIMARY KEY(id)
-- );
-- ****************************************************** from pgAdmin:
-- CREATE TABLE IF NOT EXISTS public.text_parts
-- (
--   id integer NOT NULL,
--   body text,
--   CONSTRAINT text_parts_pkey PRIMARY KEY (id)
-- )
-- WITH (
-- OIDS=FALSE
-- );
-- ALTER TABLE public.text_parts
--   OWNER TO postgres;
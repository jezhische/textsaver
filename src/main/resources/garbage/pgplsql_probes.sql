-- CREATE EXTENSION IF NOT EXISTS plpgsql;

-- DROP FUNCTION IF EXISTS concat_selected_fields(in_t public.text_parts);

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


CREATE OR REPLACE FUNCTION concat_selected_fields(in_t public.text_parts) RETURNS text AS $$
BEGIN
  RAISE NOTICE 'body = %', in_t.body;
  RETURN in_t.id || in_t.body || in_t.next_item;
END;
$$ LANGUAGE plpgsql;

SELECT concat_selected_fields(tp.*) FROM public.text_parts AS tp WHERE tp.id = 28;



-- SETOF - set of (tables in this case)
-- get_tp_by_id(VARIADIC id INTEGER[])  (must be like WHERE tp.id = get_tp_by_id.id[2]), then SELECT get_tp_by_id(28, 29);
-- user_id users.user_id%TYPE;     имя имя_таблицы%ROWTYPE;
CREATE OR REPLACE FUNCTION get_tp_by_id(id INTEGER) RETURNS SETOF public.text_parts AS $$
BEGIN
  RETURN QUERY SELECT * FROM public.text_parts AS tp WHERE tp.id = get_tp_by_id.id;
END
$$ LANGUAGE plpgsql;

SELECT get_tp_by_id(28);






CREATE OR REPLACE FUNCTION get_all_textparts() RETURNS SETOF public.text_parts AS
$BODY$
DECLARE
  r public.text_parts%rowtype;
BEGIN
--   вставить единичное значение в результирующий набор:
  RETURN QUERY SELECT * FROM public.text_parts AS tp WHERE tp.id = 28;
  FOR r IN
  SELECT * FROM public.text_parts AS tp WHERE tp.id IN (30, 29)
  LOOP
    -- здесь возможна обработка данных;
    RAISE NOTICE 'current text_parts.id = %', r.id;

    RETURN NEXT r; -- добавляет текущую строку запроса к возвращаемому результату

  END LOOP;
  RETURN;
END
$BODY$
LANGUAGE plpgsql;

SELECT * FROM get_all_textparts();


CREATE OR REPLACE FUNCTION get_textparts_ordered_set(start_id INTEGER, size INTEGER) RETURNS SETOF public.text_parts AS
$body$
-- the label of the outer block
<<external>>
DECLARE
  r public.text_parts%ROWTYPE;
  next_id INTEGER;
BEGIN
  next_id := start_id;
  FOR i IN 1...5
  LOOP
--     EXIT external WHEN next_id is NULL;
    RAISE NOTICE 'i = %', i;
--     i := i + 1;
--     r := (SELECT tp FROM public.text_parts AS tp WHERE id = next_id);
--     casting to text_parts to specify type explicitly to get field next_item from r composite type
--     next_id := (r::text_parts).next_item;
    RAISE NOTICE 'next_id = %', next_id;
--     RETURN NEXT r;
  END LOOP;
  RETURN;
END
$body$
LANGUAGE plpgsql;

SELECT * FROM get_textparts_ordered_set(28, 10);

CREATE TABLE IF NOT EXISTS text_common_data (
    id SERIAL PRIMARY KEY
  , name VARCHAR(255)
  , creating_date TIMESTAMP without time zone DEFAULT now()
  , updating_date TIMESTAMP without time zone DEFAULT now()
);

CREATE TABLE IF NOT EXISTS text_parts (
  id SERIAL PRIMARY KEY
  , body TEXT
  , previous_item BIGINT UNIQUE
  , next_item BIGINT UNIQUE
  , text_common_data_id BIGINT
  , last_update TIMESTAMP without time zone DEFAULT now()
-- FOREIGN KEY (text_common_data_id) - ссылка на колонку в этой таблице
  , CONSTRAINT fk_textParts_textCommonData FOREIGN KEY (text_common_data_id) REFERENCES text_common_data (id)
);





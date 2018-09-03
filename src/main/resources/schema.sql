CREATE TABLE IF NOT EXISTS text_parts (
  id SERIAL PRIMARY KEY,
  body TEXT,
  previous BIGINT,
  next BIGINT
);
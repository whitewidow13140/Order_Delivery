CREATE SCHEMA IF NOT EXISTS order_manager;

CREATE TABLE IF NOT EXISTS order_manager.orders (
  id          BIGSERIAL PRIMARY KEY,
  item        VARCHAR(255) NOT NULL,
  quantity    INTEGER      NOT NULL,
  status      VARCHAR(32),
  created_at  TIMESTAMPTZ
);

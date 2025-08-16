CREATE SCHEMA IF NOT EXISTS delivery_tracker;

CREATE TABLE IF NOT EXISTS delivery_tracker.deliveries (
  id          BIGSERIAL PRIMARY KEY,
  order_id    BIGINT       NOT NULL,
  item        VARCHAR(255) NOT NULL,
  quantity    INTEGER      NOT NULL,
  status      VARCHAR(32)  NOT NULL,
  created_at  TIMESTAMPTZ  NOT NULL,
  updated_at  TIMESTAMPTZ  NOT NULL
);

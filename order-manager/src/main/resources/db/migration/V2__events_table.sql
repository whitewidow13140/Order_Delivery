CREATE TABLE IF NOT EXISTS order_manager.events (
  id              BIGSERIAL PRIMARY KEY,
  correlation_id  UUID        NOT NULL,
  order_id        BIGINT      NULL,
  event_type      TEXT        NOT NULL,
  payload         JSONB       NOT NULL,
  occurred_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  produced_by     TEXT        NOT NULL DEFAULT 'order-manager',
  headers         JSONB       NULL,
  version         INT         NOT NULL DEFAULT 1
);

CREATE INDEX IF NOT EXISTS idx_om_events_corr  ON order_manager.events (correlation_id);
CREATE INDEX IF NOT EXISTS idx_om_events_oid   ON order_manager.events (order_id);
CREATE INDEX IF NOT EXISTS idx_om_events_type_time ON order_manager.events (event_type, occurred_at DESC);

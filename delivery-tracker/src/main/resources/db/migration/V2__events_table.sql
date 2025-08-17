CREATE TABLE IF NOT EXISTS delivery_tracker.events (
  id              BIGSERIAL PRIMARY KEY,
  correlation_id  UUID        NOT NULL,
  order_id        BIGINT      NULL,
  event_type      TEXT        NOT NULL,
  payload         JSONB       NOT NULL,
  occurred_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  produced_by     TEXT        NOT NULL DEFAULT 'delivery-tracker',
  headers         JSONB       NULL,
  version         INT         NOT NULL DEFAULT 1
);

CREATE INDEX IF NOT EXISTS idx_dt_events_corr  ON delivery_tracker.events (correlation_id);
CREATE INDEX IF NOT EXISTS idx_dt_events_oid   ON delivery_tracker.events (order_id);
CREATE INDEX IF NOT EXISTS idx_dt_events_type_time ON delivery_tracker.events (event_type, occurred_at DESC);

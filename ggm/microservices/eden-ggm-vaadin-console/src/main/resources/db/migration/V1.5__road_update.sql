ALTER TABLE road ADD COLUMN IF NOT EXISTS exclude_clusters text;
ALTER TABLE road ADD COLUMN IF NOT EXISTS group_name character varying(255);
ALTER TABLE road ADD COLUMN IF NOT EXISTS order_position integer;
ALTER TABLE road ADD COLUMN IF NOT EXISTS search_syntax character varying(255);
ALTER TABLE road ADD COLUMN IF NOT EXISTS from_node integer;
ALTER TABLE road ADD COLUMN IF NOT EXISTS to_node integer;

ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS exclude_clusters text;
ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS group_name character varying(255);
ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS order_position integer;
ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS search_syntax character varying(255);
ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS from_node integer;
ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS to_node integer;
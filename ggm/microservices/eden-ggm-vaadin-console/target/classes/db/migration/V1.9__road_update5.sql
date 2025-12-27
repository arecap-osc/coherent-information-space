ALTER TABLE road_aud DROP COLUMN manual_nodes;
ALTER TABLE road ADD COLUMN IF NOT EXISTS manual_nodes text;
ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS manual_nodes text;
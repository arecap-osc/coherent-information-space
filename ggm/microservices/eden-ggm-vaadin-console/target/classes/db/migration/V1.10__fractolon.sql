ALTER TABLE road DROP COLUMN manual_nodes;
ALTER TABLE road_aud DROP COLUMN manual_nodes;

ALTER TABLE road_aud ADD COLUMN IF NOT EXISTS fractolon text;
ALTER TABLE road ADD COLUMN IF NOT EXISTS fractolon text;
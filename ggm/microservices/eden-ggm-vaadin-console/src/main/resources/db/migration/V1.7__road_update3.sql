ALTER TABLE road DROP COLUMN manual_road;
ALTER TABLE road ADD COLUMN IF NOT EXISTS manual_nodes text;

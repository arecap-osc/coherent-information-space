drop table map_link;
drop table map_word;
drop TABLE semantic_map;

ALTER TABLE grid RENAME TO semantic_map;
ALTER TABLE semantic_map RENAME COLUMN grid_id TO semantic_map_id;
ALTER TABLE semantic_map RENAME CONSTRAINT grid_pkey TO semantic_map_pkey;

ALTER TABLE grid_letter RENAME TO map_word;
ALTER TABLE map_word RENAME COLUMN grid_letter_id TO map_word_id;
ALTER TABLE map_word RENAME COLUMN semantic TO word;
ALTER TABLE map_word RENAME COLUMN grid_id TO semantic_map_id;
ALTER TABLE map_word RENAME CONSTRAINT grid_letter_pkey TO map_word_pkey;

ALTER TABLE grid_projection RENAME TO map_link;
ALTER TABLE map_link RENAME COLUMN grid_projection_id TO map_link_id;
ALTER TABLE map_link RENAME COLUMN from_grid_letter_id TO from_map_word_id;
ALTER TABLE map_link RENAME COLUMN to_grid_letter_id TO to_map_word_id;
ALTER TABLE map_link RENAME CONSTRAINT grid_projection_pkey TO map_link_pkey;


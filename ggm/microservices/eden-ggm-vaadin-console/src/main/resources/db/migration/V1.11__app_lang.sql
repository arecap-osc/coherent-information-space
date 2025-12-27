ALTER TABLE application ADD COLUMN language text default 'ro';
ALTER TABLE application ADD COLUMN related_application_id bigint;

ALTER TABLE application_aud ADD COLUMN language text;
ALTER TABLE application_aud ADD COLUMN related_application_id bigint;
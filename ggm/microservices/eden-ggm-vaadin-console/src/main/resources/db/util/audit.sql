select tblename, dat, username, to_timestamp("timestamp" / 1000) from
(
select 'road_aud' as tblename, r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join road_aud AS r on e.id = r.rev union all
select 'application_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join application_aud AS r on e.id = r.rev union all
select 'application_data_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join application_data_aud AS r on e.id = r.rev union all
select 'document_image_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join document_image_aud AS r on e.id = r.rev union all
select 'document_image_content_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join document_image_content_aud AS r on e.id = r.rev union all
select 'etl_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join etl_aud AS r on e.id = r.rev union all
select 'feedback_application_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join feedback_application_aud AS r on e.id = r.rev union all
select 'feedback_data_map_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join feedback_data_map_aud AS r on e.id = r.rev union all
select 'map_link_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join map_link_aud AS r on e.id = r.rev union all
select 'map_word_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join map_word_aud AS r on e.id = r.rev union all
select 'semantic_map_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join semantic_map_aud AS r on e.id = r.rev union all
select 'unicursal_data_map_aud', r::text as dat, e.username, e.timestamp from envers_rev_entity e inner join unicursal_data_map_aud AS r on e.id = r.rev
) k
order by "timestamp" desc limit 1000;


select 'select array_to_string(translate(string_to_array(r::text, '','')::text, ''()'', '''')::text[], ''|'') as dat from ' || tablename || ' AS r union all' from pg_tables where tablename like '%_aud';
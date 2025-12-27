CREATE TABLE envers_rev_entity ( id integer NOT NULL, "timestamp" bigint NOT NULL, username character varying(255), CONSTRAINT envers_rev_entity_pkey PRIMARY KEY (id));

create table road_aud (id int8 not null, rev int4 not null, revtype int2, name varchar(255), network varchar(255), road varchar(255), primary key (id, rev));
create table application_aud (application_id int8 not null, rev int4 not null, revtype int2, etl_version int4, original_application int8, semantic_grid_id int8, primary key (application_id, rev));
create table application_data_aud (application_data_id int8 not null, rev int4 not null, revtype int2, address_index int8, cluster_index int8, cluster_semantic text, conjunction text, data_map_id int8, how text, network varchar(255), network_semantic text, phrase text, semantic text, semantic_details text, syntax text, syntax_details text, syntax_done boolean, to_address_index int8, verb text, verb_details text, verbalization text, where_when text, who_what text, why text, application_id int8, primary key (application_data_id, rev));
create table document_image_aud (application_id int8 not null, id int8 not null, rev int4 not null, revtype int2, date timestamp, explanation varchar(255), name varchar(255), parent_doc_class varchar(255), parent_id int8, source varchar(255), url varchar(255), user_id int8, image_content_application_id int8, image_content_id int8, primary key (application_id, id, rev));
create table document_image_content_aud (application_id int8 not null, id int8 not null, rev int4 not null, revtype int2, image text, primary key (application_id, id, rev));
create table etl_aud (etl_id int8 not null, rev int4 not null, revtype int2, brief text, description text, etl_log text, label text, level int4, node int4, original_node int4, used_in_application boolean, application_id int8, primary key (etl_id, rev));
create table feedback_application_aud (feedback_application_id int8 not null, rev int4 not null, revtype int2, brief text, description text, label text, primary key (feedback_application_id, rev));
create table feedback_data_map_aud (feedback_data_map_id int8 not null, rev int4 not null, revtype int2, application_id int8, baze_experiente varchar(255), baze_strategii varchar(255), feedback_column int4, complete_semantic text, dimenssion varchar(255), done boolean, evaluare_raspunsuri varchar(255), feedback_position int8, iesire_date varchar(255), in_out_semantic text, intrare_date varchar(255), out_in_semantic text, procesare_date varchar(255), feedback_row int4, unicursal varchar(255), unicursal_green varchar(255), unicursal_purple varchar(255), primary key (feedback_data_map_id, rev));
create table map_link_aud (map_link_id int8 not null, rev int4 not null, revtype int2, conjunction text, details text, phrase text, verb text, verbalization text, from_map_word_id int8, to_map_word_id int8, primary key (map_link_id, rev));
create table map_word_aud (map_word_id int8 not null, rev int4 not null, revtype int2, details text, letter varchar(255), word text, x int8, y int8, semantic_map_id int8, primary key (map_word_id, rev));
create table semantic_map_aud (semantic_map_id int8 not null, rev int4 not null, revtype int2, brief text, description text, label text, primary key (semantic_map_id, rev));
create table unicursal_data_map_aud (unicursal_data_map_id int8 not null, rev int4 not null, revtype int2, application_id int8, feedback_column int4, definition varchar(255), dimenssion text, dimenssion_url text, feedback_row int4, semantic text, unicursal text, unicursal_green text, unicursal_purple text, unicursal_url text, primary key (unicursal_data_map_id, rev));

alter table road_aud add constraint FKtq2sl5g12sj38udqqnmve8k7o foreign key (rev) references envers_rev_entity;
alter table application_aud add constraint FKp908xu879qljr5nc0eq2pc7na foreign key (rev) references envers_rev_entity;
alter table application_data_aud add constraint FK4pt6ndrq1cr6ky2xygcshvcic foreign key (rev) references envers_rev_entity;
alter table document_image_aud add constraint FK2l9trp1acoc1aeye3nlu8fqvv foreign key (rev) references envers_rev_entity;
alter table document_image_content_aud add constraint FKgqil3hygh04tfhpbutqfer327 foreign key (rev) references envers_rev_entity;
alter table etl_aud add constraint FKf5nhkkraf5bs9t5f6erigmkld foreign key (rev) references envers_rev_entity;
alter table feedback_application_aud add constraint FK8kohkcablaxoro52v364utxcl foreign key (rev) references envers_rev_entity;
alter table feedback_data_map_aud add constraint FKvmls7vn09mo9028u70qqqpnl foreign key (rev) references envers_rev_entity;
alter table map_link_aud add constraint FK3ca9xm9a6jym297ef35k9w8kg foreign key (rev) references envers_rev_entity;
alter table map_word_aud add constraint FKr5jn5f6i0fwsvh6ukq6ulj8pc foreign key (rev) references envers_rev_entity;
alter table semantic_map_aud add constraint FKa7n09dwyg1lsir58fuulatn0q foreign key (rev) references envers_rev_entity;
alter table unicursal_data_map_aud add constraint FKocxhuoo8auokx3w8l74ssvses foreign key (rev) references envers_rev_entity;

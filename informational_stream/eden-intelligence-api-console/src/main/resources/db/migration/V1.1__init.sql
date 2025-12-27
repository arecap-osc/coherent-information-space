---sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;

-- envers audit config
CREATE TABLE IF NOT EXISTS envers_rev_entity
(
    id integer NOT NULL,
    "timestamp" bigint NOT NULL,
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT envers_rev_entity_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

--ff4j entities

CREATE TABLE IF NOT EXISTS ff4j_audit
(
    evt_uuid character varying(40) COLLATE pg_catalog."default" NOT NULL,
    evt_time timestamp without time zone NOT NULL,
    evt_type character varying(30) COLLATE pg_catalog."default" NOT NULL,
    evt_name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    evt_action character varying(30) COLLATE pg_catalog."default" NOT NULL,
    evt_hostname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    evt_source character varying(30) COLLATE pg_catalog."default" NOT NULL,
    evt_duration integer,
    evt_user character varying(30) COLLATE pg_catalog."default",
    evt_value character varying(100) COLLATE pg_catalog."default",
    evt_keys character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT ff4j_audit_pkey PRIMARY KEY (evt_uuid, evt_time)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS ff4j_features
(
    feat_uid character varying(100) COLLATE pg_catalog."default" NOT NULL,
    enable integer NOT NULL,
    description character varying(1000) COLLATE pg_catalog."default",
    strategy character varying(1000) COLLATE pg_catalog."default",
    expression character varying(255) COLLATE pg_catalog."default",
    groupname character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT ff4j_features_pkey PRIMARY KEY (feat_uid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS ff4j_properties
(
    property_id character varying(100) COLLATE pg_catalog."default" NOT NULL,
    clazz character varying(255) COLLATE pg_catalog."default" NOT NULL,
    currentvalue character varying(255) COLLATE pg_catalog."default",
    fixedvalues character varying(1000) COLLATE pg_catalog."default",
    description character varying(1000) COLLATE pg_catalog."default",
    CONSTRAINT ff4j_properties_pkey PRIMARY KEY (property_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS ff4j_custom_properties
(
    property_id character varying(100) COLLATE pg_catalog."default" NOT NULL,
    clazz character varying(255) COLLATE pg_catalog."default" NOT NULL,
    currentvalue character varying(255) COLLATE pg_catalog."default",
    fixedvalues character varying(1000) COLLATE pg_catalog."default",
    description character varying(1000) COLLATE pg_catalog."default",
    feat_uid character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT ff4j_custom_properties_pkey PRIMARY KEY (property_id, feat_uid),
    CONSTRAINT ff4j_custom_properties_feat_uid_fkey FOREIGN KEY (feat_uid)
        REFERENCES ff4j_features (feat_uid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;



CREATE TABLE IF NOT EXISTS ff4j_roles
(
    feat_uid character varying(100) COLLATE pg_catalog."default" NOT NULL,
    role_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT ff4j_roles_pkey PRIMARY KEY (feat_uid, role_name),
    CONSTRAINT ff4j_roles_feat_uid_fkey FOREIGN KEY (feat_uid)
        REFERENCES ff4j_features (feat_uid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS flyway_schema_history
(
    installed_rank integer NOT NULL,
    version character varying(50) COLLATE pg_catalog."default",
    description character varying(200) COLLATE pg_catalog."default" NOT NULL,
    type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    script character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    checksum integer,
    installed_by character varying(100) COLLATE pg_catalog."default" NOT NULL,
    installed_on timestamp without time zone NOT NULL DEFAULT now(),
    execution_time integer NOT NULL,
    success boolean NOT NULL,
    CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

--attachments entities

CREATE TABLE IF NOT EXISTS document_image
(
    application_id bigint NOT NULL,
    id bigint NOT NULL,
    date timestamp without time zone,
    explanation character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    parent_doc_class character varying(255) COLLATE pg_catalog."default",
    parent_id bigint,
    source character varying(255) COLLATE pg_catalog."default",
    url character varying(255) COLLATE pg_catalog."default",
    user_id bigint,
    image_content_application_id bigint,
    image_content_id bigint,
    CONSTRAINT document_image_pkey PRIMARY KEY (application_id, id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS document_image_aud
(
    application_id bigint NOT NULL,
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    date timestamp without time zone,
    explanation character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    parent_doc_class character varying(255) COLLATE pg_catalog."default",
    parent_id bigint,
    source character varying(255) COLLATE pg_catalog."default",
    url character varying(255) COLLATE pg_catalog."default",
    user_id bigint,
    image_content_application_id bigint,
    image_content_id bigint,
    CONSTRAINT document_image_aud_pkey PRIMARY KEY (application_id, id, rev)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS document_image_content
(
    application_id bigint NOT NULL,
    id bigint NOT NULL,
    image text COLLATE pg_catalog."default",
    CONSTRAINT document_image_content_pkey PRIMARY KEY (application_id, id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS document_image_content_aud
(
    application_id bigint NOT NULL,
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    image text COLLATE pg_catalog."default",
    CONSTRAINT document_image_content_aud_pkey PRIMARY KEY (application_id, id, rev)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- application entities

CREATE TABLE IF NOT EXISTS informational_stream
(
    informational_stream_id bigint NOT NULL,
    functional_description text COLLATE pg_catalog."default",
    locale character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    i18n_id bigint,
    CONSTRAINT neural_network_pkey PRIMARY KEY (informational_stream_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS informational_stream_aud
(
    informational_stream_id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    functional_description text COLLATE pg_catalog."default",
    locale character varying(255) COLLATE pg_catalog."default",
    i18n_id bigint,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT informational_stream_aud_pkey PRIMARY KEY (informational_stream_id, rev),
    CONSTRAINT informational_stream_aud_rev_fkey FOREIGN KEY (rev)
        REFERENCES envers_rev_entity (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


CREATE TABLE IF NOT EXISTS feature
(
    feature_id bigint NOT NULL,
    content text COLLATE pg_catalog."default",
    informational_stream_id bigint,
    i18n_id bigint,
    CONSTRAINT feature_pkey PRIMARY KEY (feature_id),
    CONSTRAINT fk4dmhoa66ntg7fqbsx1csd4o1n FOREIGN KEY (informational_stream_id)
        REFERENCES informational_stream (informational_stream_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS feature_aud
(
    feature_id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    i18n_id bigint,
    content text COLLATE pg_catalog."default",
    informational_stream_id bigint,
    CONSTRAINT feature_aud_pkey PRIMARY KEY (feature_id, rev),
    CONSTRAINT feature_aud_rev_fkey FOREIGN KEY (rev)
        REFERENCES envers_rev_entity (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS signal
(
    signal_id bigint NOT NULL,
    details character varying(255) COLLATE pg_catalog."default",
    feature_part character varying(255) COLLATE pg_catalog."default",
    stream_application_type character varying(255) COLLATE pg_catalog."default",
    feature_id bigint,
    informational_stream_id bigint,
    i18n_id bigint,
    locale character varying(255) COLLATE pg_catalog."default",
    congruence_id bigint,
    similarity_id bigint,
    CONSTRAINT signal_pkey PRIMARY KEY (signal_id),
    CONSTRAINT fkmjrxavk35xi34e26rtow8urcw FOREIGN KEY (informational_stream_id)
        REFERENCES informational_stream (informational_stream_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkpkyq74ckgc3dru2g1dx1yroa7 FOREIGN KEY (feature_id)
        REFERENCES feature (feature_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS signal_aud
(
    signal_id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    i18n_id bigint,
    details character varying(255) COLLATE pg_catalog."default",
    feature_part character varying(255) COLLATE pg_catalog."default",
    stream_application_type character varying(255) COLLATE pg_catalog."default",
    feature_id bigint,
    informational_stream_id bigint,
    locale character varying(255) COLLATE pg_catalog."default",
    congruence_id bigint,
    similarity_id bigint,
    CONSTRAINT signal_aud_pkey PRIMARY KEY (signal_id, rev),
    CONSTRAINT signal_aud_rev_fkey FOREIGN KEY (rev)
        REFERENCES envers_rev_entity (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS feature_stream
(
    feature_stream_id bigint NOT NULL,
    downstream bigint,
    upstream bigint,
    topology character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT synapse_pkey PRIMARY KEY (feature_stream_id),
    CONSTRAINT fka4850mefapsk4u0s555gm1y7g FOREIGN KEY (upstream)
        REFERENCES signal (signal_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkn657megif5mplt0ti350efb9x FOREIGN KEY (downstream)
        REFERENCES signal (signal_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS feature_stream_aud
(
    feature_stream_id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    downstream bigint,
    upstream bigint,
    topology character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT feature_stream_aud_pkey PRIMARY KEY (feature_stream_id, rev),
    CONSTRAINT feature_stream_aud_rev_fkey FOREIGN KEY (rev)
        REFERENCES envers_rev_entity (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- main table to store features
create table ff4j_features (
  "feat_uid"     	varchar(100),
  "enable"  		integer not null,
  "description" 	varchar(1000),
  "strategy"		varchar(1000),
  "expression"	    varchar(255),
  "groupname"		varchar(100),
  primary key("feat_uid")
);

-- roles to store acl, fk to main table
create table ff4j_roles (
  "feat_uid"     varchar(100) references ff4j_features("feat_uid"),
  "role_name"    varchar(100),
  primary key("feat_uid", "role_name")
);

-- feature internal custom properties
create table ff4j_custom_properties (
  "property_id"  varchar(100) not null,
  "clazz" 		 varchar(255) not null,
  "currentvalue" varchar(255),
  "fixedvalues"	 varchar(1000),
  "description"	 varchar(1000),
  "feat_uid"     varchar(100) references ff4j_features("feat_uid"),
  primary key("property_id", "feat_uid")
);

-- @propertystore (edit general properties)
create table ff4j_properties (
  "property_id"  varchar(100) not null,
  "clazz" 		 varchar(255) not null,
  "currentvalue" varchar(255),
  "fixedvalues"	 varchar(1000),
  "description"	 varchar(1000),
  primary key("property_id")
);

-- @see jdbceventrepository (audit event)
create table ff4j_audit (
  "evt_uuid" 	 varchar(40)  not null,
  "evt_time" 	 timestamp 	  not null,
  "evt_type" 	 varchar(30)  not null,
  "evt_name" 	 varchar(30)  not null,
  "evt_action" 	 varchar(30)  not null,
  "evt_hostname" varchar(100)  not null,
  "evt_source" 	 varchar(30)  not null,
  "evt_duration" integer,
  "evt_user" 	 varchar(30),
  "evt_value" 	 varchar(100),
  "evt_keys" 	 varchar(255),
  primary key("evt_uuid", "evt_time")
);
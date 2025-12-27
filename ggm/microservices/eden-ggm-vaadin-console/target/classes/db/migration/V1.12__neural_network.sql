CREATE TABLE IF NOT EXISTS neural_network (
	neural_network_id int8 NOT NULL,
	functional_description text NULL,
	locale varchar(255) NULL,
	"name" varchar(255) NULL,
	CONSTRAINT neural_network_pkey PRIMARY KEY (neural_network_id)
);

CREATE TABLE IF NOT EXISTS feature (
	feature_id int8 NOT NULL,
	"content" text NULL,
	neural_network_id int8 NULL,
	CONSTRAINT feature_pkey PRIMARY KEY (feature_id),
	CONSTRAINT fk4dmhoa66ntg7fqbsx1csd4o1n FOREIGN KEY (neural_network_id) REFERENCES neural_network(neural_network_id)
);

CREATE TABLE IF NOT EXISTS congruence_signal (
	congruence_signal_id int8 NOT NULL,
	details text NULL,
	feature_part varchar(255) NULL,
	CONSTRAINT congruence_signal_pkey PRIMARY KEY (congruence_signal_id)
);

CREATE TABLE IF NOT EXISTS signal_similarity (
	signal_similarity_id int8 NOT NULL,
	details text NULL,
	feature_part varchar(255) NULL,
	CONSTRAINT signal_similarity_pkey PRIMARY KEY (signal_similarity_id)
);

CREATE TABLE IF NOT EXISTS signal (
	signal_id int8 NOT NULL,
	details varchar(255) NULL,
	feature_part varchar(255) NULL,
	neuron_topology varchar(255) NULL,
	neuron_type varchar(255) NULL,
	congruence_signal_id int8 NULL,
	feature_id int8 NULL,
	neural_network_id int8 NULL,
	signal_similarity_id int8 NULL,
	CONSTRAINT signal_pkey PRIMARY KEY (signal_id),
	CONSTRAINT fkgotsgn2ectqs6nq30x260umnb FOREIGN KEY (signal_similarity_id) REFERENCES signal_similarity(signal_similarity_id),
	CONSTRAINT fkmjrxavk35xi34e26rtow8urcw FOREIGN KEY (neural_network_id) REFERENCES neural_network(neural_network_id),
	CONSTRAINT fkpkyq74ckgc3dru2g1dx1yroa7 FOREIGN KEY (feature_id) REFERENCES feature(feature_id),
	CONSTRAINT fkpuk0hj4advmfq7j8vtcjqpbat FOREIGN KEY (congruence_signal_id) REFERENCES congruence_signal(congruence_signal_id)
);

CREATE TABLE IF NOT EXISTS synapse (
	synapse_id int8 NOT NULL,
	downstream int8 NULL,
	upstream int8 NULL,
	CONSTRAINT synapse_pkey PRIMARY KEY (synapse_id),
	CONSTRAINT fka4850mefapsk4u0s555gm1y7g FOREIGN KEY (upstream) REFERENCES signal(signal_id),
	CONSTRAINT fkn657megif5mplt0ti350efb9x FOREIGN KEY (downstream) REFERENCES signal(signal_id)
);

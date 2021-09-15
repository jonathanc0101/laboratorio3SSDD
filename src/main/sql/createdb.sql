-- CREATE USER borderoadm WITH PASSWORD 'borderoadm';

-- CREATE DATABASE borderodb WITH OWNER borderoadm;

CREATE TABLE public.customer
(
    id     int8    NOT NULL,
    "name" varchar NULL,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE SEQUENCE public.customer_sq
    INCREMENT BY 5
    MINVALUE 1
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE public.play
(
    id     int8    NOT NULL,
    code   varchar NOT NULL,
    "name" varchar NULL,
    "type" varchar NULL,
    CONSTRAINT play_pk PRIMARY KEY (id)
);

CREATE SEQUENCE public.play_sq
    INCREMENT BY 5
    MINVALUE 1
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE public.bordero
(
    id          int8 NOT NULL,
    "date"      date NULL,
    customer_id int8 NULL,
    CONSTRAINT bordero_pk PRIMARY KEY (id),
    CONSTRAINT bordero_fk FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE SEQUENCE public.bordero_sq
    INCREMENT BY 5
    MINVALUE 1
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE public.performance
(
    id         int8 NOT NULL,
    bordero_id int8 NOT NULL,
    play_id    int8 NULL,
    audience   int2 NULL,
    CONSTRAINT performance_pk PRIMARY KEY (id),
    CONSTRAINT performance_fk1 FOREIGN KEY (bordero_id) REFERENCES bordero (id),
    CONSTRAINT performance_fk2 FOREIGN KEY (play_id) REFERENCES play (id)
);

CREATE SEQUENCE public.performance_sq
    INCREMENT BY 5
    MINVALUE 1
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE boss (
                id integer PRIMARY KEY,
                login varchar(255) NOT NULL,
                password varchar(255) NOT NULL);

CREATE TABLE worker (
                id integer PRIMARY KEY,
                name character(100) NOT NULL,
                salary real NOT NULL);

CREATE TABLE shift (
                id integer PRIMARY KEY,
                name varchar(100) NOT NULL,
                shift_date long NOT NULL,
                boss_id integer NOT NULL,
                CONSTRAINT boss_fk FOREIGN KEY (boss_id)
                REFERENCES boss(id) ON DELETE CASCADE);

CREATE TABLE machine (
                id integer PRIMARY KEY,
                machine_type character(100) NOT NULL,
                shift_begin_time character(100) NOT NULL,
                shift_end_time character(100) NOT NULL,
                shift_id integer NOT NULL,
                shift_name character(100) NOT NULL,
                CONSTRAINT shift_fk FOREIGN KEY (shift_id)
                REFERENCES shift(id) ON DELETE CASCADE);

CREATE TABLE machine_workers (
                id integer PRIMARY KEY,
                machine_id integer NOT NULL,
                worker_id integer NOT NULL,
                worker_name character(100) NOT NULL,
                hours integer NOT NULL,
                CONSTRAINT machine_fk FOREIGN KEY (machine_id)
                REFERENCES machine(id) ON DELETE CASCADE,
                CONSTRAINT worker_fk FOREIGN KEY (worker_id)
                REFERENCES worker(id) ON DELETE CASCADE);
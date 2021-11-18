CREATE TABLE IF NOT EXISTS water_sample
(
    id          INT NOT NULL auto_increment,
    temperature FLOAT,
    location    VARCHAR (22),
    date        TIMESTAMP,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS light_switch
(
    id          INT NOT NULL auto_increment,
    room_name   VARCHAR(25),
    floor       TINYINT,
    on_code     INT,
    off_code    INT,
    toggle_code INT,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS users
(
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled  TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY (username)
    );

CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
    );

CREATE TABLE IF NOT EXISTS plug
(
    id          INT NOT NULL auto_increment,
    name        VARCHAR(25),
    location    VARCHAR(25),
    description VARCHAR(2048),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS plug_energy
(
    id INT NOT NULL auto_increment,
    sample_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_start_time TIMESTAMP,
    total FLOAT,
    yesterday FLOAT,
    period         INT,
    power          INT,
    apparent_power INT,
    reactive_power INT,
    factor FLOAT,
    voltage INT,
    current FLOAT,
    plug INT,
    FOREIGN KEY (plug) REFERENCES plug (id) ,
    PRIMARY KEY (id)
    );

SELECT If (EXISTS(SELECT DISTINCT index_name
                  FROM   information_schema.statistics
                  WHERE  table_schema = 'homeauto'
                    AND table_name = 'authorities'
                    AND index_name LIKE 'ix_auth_username'),
           'select ''index ix_auth_username exists'' _______;',
           'create unique index ix_auth_username on authorities(username,authority)'
           )
INTO   @a;

PREPARE stmt1 FROM @a;
EXECUTE stmt1;
DEALLOCATE PREPARE stmt1;


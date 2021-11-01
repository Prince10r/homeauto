create table if not exists water_sample(
     id INT NOT NULL AUTO_INCREMENT,
     temperature FLOAT,
     location VARCHAR (22),
     date TIMESTAMP,
     PRIMARY KEY (id)
);
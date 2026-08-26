CREATE TABLE evg_auth_users
(
    id int(20) PRIMARY KEY AUTO_INCREMENT,
    username varchar(255),
    password varchar(255),
    enable bit(1) NOT NULL,
    locked bit(1) NOT NULL
);

CREATE TABLE evg_auth_authorities
(
    id int(20) PRIMARY KEY AUTO_INCREMENT,
    authority varchar(255),
    user_entity_id int(20),
    CONSTRAINT fk_user FOREIGN KEY (user_entity_id) REFERENCES evg_auth_users(id)
);



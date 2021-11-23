CREATE DATABASE car_app;
GO;

USE car_app;

CREATE TABLE admin
(
    username  varchar(100),
    password  varchar(100),
    suspended bit,
    PRIMARY KEY (username)
);

CREATE TABLE customer
(
    username     varchar(100),
    password     varchar(100),
    email        varchar(100),
    phone_number varchar(100),
    suspended    bit,
    PRIMARY KEY (username),
);

CREATE TABLE driver
(
    username    varchar(100),
    password    varchar(100),
    email       varchar(100),
    phone       varchar(100),
    national_id varchar(100),
    license     varchar(100),
    suspended   bit,
    verified    bit,
    PRIMARY KEY (username),
);

CREATE TABLE rate
(
    user_id    varchar(100),
    driver_id  varchar(100),
    rate_value float,
    PRIMARY KEY (user_id, driver_id),
    FOREIGN KEY (driver_id) REFERENCES driver (username),
    FOREIGN KEY (user_id) REFERENCES customer (username)
);

CREATE TABLE request
(
    request_id  int IDENTITY (1,1),
    source      varchar(100),
    destination varchar(100),
    user_id     varchar(100),
    PRIMARY KEY (request_id),
    FOREIGN KEY (user_id) REFERENCES customer (username)
);

CREATE TABLE offer
(
    offer_id  int IDENTITY (1,1),
    driver_id varchar(100),
    accepted   bit,
    ride_id   int,
    PRIMARY KEY (offer_id),
    price     float,
    FOREIGN KEY (driver_id) REFERENCES driver (username),
    FOREIGN KEY (ride_id) REFERENCES request (request_id)
);

CREATE TABLE favourite_places
(
    favourite_place varchar(100),
    driver_id       varchar(100),
    PRIMARY KEY (favourite_place, driver_id),
    FOREIGN KEY (driver_id) REFERENCES driver (username),
);

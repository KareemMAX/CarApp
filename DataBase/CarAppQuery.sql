CREATE DATABASE car_app;
GO;

USE car_app;

CREATE TABLE admin
(
    user_id   int IDENTITY (1,1),
    username  varchar(100),
    password  varchar(100),
    suspended bit,
    PRIMARY KEY (user_id)
);

CREATE TABLE customer
(
    customer_id  int IDENTITY (1,1),
    username     varchar(100),
    password     varchar(100),
    email        varchar(100),
    phone_number varchar(100),
    suspended    bit,
    PRIMARY KEY (customer_id),
);

CREATE TABLE driver
(
    driver_id   int IDENTITY (1,1),
    username    varchar(100),
    password    varchar(100),
    email       varchar(100),
    phone       varchar(100),
    national_id varchar(100),
    license     varchar(100),
    suspended   bit,
    verified    bit,
    PRIMARY KEY (driver_id),
);

CREATE TABLE rate
(
    user_id    int,
    driver_id  int,
    rate_value float,
    PRIMARY KEY (user_id, driver_id),
    FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
    FOREIGN KEY (user_id) REFERENCES customer (customer_id)
);

CREATE TABLE request
(
    request_id  int IDENTITY (1,1),
    source      varchar(100),
    destination varchar(100),
    user_id     int,
    PRIMARY KEY (request_id),
    FOREIGN KEY (user_id) REFERENCES customer (customer_id)
);

CREATE TABLE offer
(
    offer_id  int IDENTITY (1,1),
    driver_id int,
    ride_id   int,
    PRIMARY KEY (offer_id),
    price     int,
    FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
    FOREIGN KEY (ride_id) REFERENCES request (request_id)
);

CREATE TABLE favourite_places
(
    favourite_place varchar(100),
    driver_id       int,
    PRIMARY KEY (favourite_place, driver_id),
    FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
);

CREATE TABLE past_rides
(
    user_id  int,
    offer_id int,
    PRIMARY KEY (user_id, offer_id),
    FOREIGN KEY (user_id) REFERENCES customer (customer_id),
    FOREIGN KEY (offer_id) REFERENCES offer (offer_id)
);
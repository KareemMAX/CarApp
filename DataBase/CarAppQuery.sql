CREATE TABLE admin
(
    userid    int,
    password  varchar(100),
    username  varchar(100),
    suspended bit,
    PRIMARY KEY (userid)
);
CREATE TABLE request
(
    ride_id     int,
    source      varchar(100),
    destination varchar(100),
    PRIMARY KEY (ride_id),

);
CREATE TABLE customer
(
    customer_id  int,
    ride_id      int,
    password     varchar(100),
    username     varchar(100),
    email        varchar(100),
    suspended    bit,
    phone_number varchar(100),
    PRIMARY KEY (customer_id),
    FOREIGN KEY (ride_id) REFERENCES request (ride_id)
);

CREATE TABLE driver
(
    driver_id int,
    password  varchar(100),
    username  varchar(100),
    email     varchar(100),
    phone     varchar(100),
    license   varchar(100),
    suspended bit,
    verified  bit,
    PRIMARY KEY (driver_id),

);
CREATE TABLE rate
(
    rate_id    int,
    driver_id  int,
    userid     int,
    PRIMARY KEY (rate_id),
    rate_value float,
    FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
    FOREIGN KEY (userid) REFERENCES customer (customer_id)
);
CREATE TABLE offer
(
    offer_id  int,
    driver_id int,
    ride_id   int,
    PRIMARY KEY (offer_id),
    price     int,
    FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
    FOREIGN KEY (ride_id) REFERENCES request (ride_id)
);
CREATE TABLE favourite_places
(
    favourite_place varchar(100),
    driver_id       int,
    PRIMARY KEY (favourite_place, driver_id),
    FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
);
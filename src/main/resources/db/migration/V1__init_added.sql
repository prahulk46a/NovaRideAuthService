CREATE TABLE booking
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime              NOT NULL,
    updated_at     datetime              NOT NULL,
    booking_status ENUM('SCHEDULED','CANCELLED','ARRIVED','ASSIGNED','IN_RIDE','COMPLETED') NULL,
    start_time     datetime              NULL,
    end_time       datetime              NULL,
    total_distance BIGINT                NULL,
    passenger_id   BIGINT                NULL,
    driver_id      BIGINT                NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE driver
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime              NOT NULL,
    updated_at     datetime              NOT NULL,
    name           VARCHAR(255)          NULL,
    license_number VARCHAR(255)          NOT NULL,
    address        VARCHAR(255)          NULL,
    CONSTRAINT pk_driver PRIMARY KEY (id)
);

CREATE TABLE passenger
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    name       VARCHAR(255)          NULL,
    CONSTRAINT pk_passenger PRIMARY KEY (id)
);

CREATE TABLE passenger_review
(
    id                       BIGINT       NOT NULL,
    passenger_review_content VARCHAR(255) NOT NULL,
    passenger_rating         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_passengerreview PRIMARY KEY (id)
);

CREATE TABLE review
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    updated_at datetime              NOT NULL,
    content    VARCHAR(255)          NOT NULL,
    rating     DOUBLE                NULL,
    booking_id BIGINT                NULL,
    CONSTRAINT pk_review PRIMARY KEY (id)
);

ALTER TABLE driver
    ADD CONSTRAINT uc_driver_licensenumber UNIQUE (license_number);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_DRIVER FOREIGN KEY (driver_id) REFERENCES driver (id);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_PASSENGER FOREIGN KEY (passenger_id) REFERENCES passenger (id);

ALTER TABLE passenger_review
    ADD CONSTRAINT FK_PASSENGERREVIEW_ON_ID FOREIGN KEY (id) REFERENCES review (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_BOOKING FOREIGN KEY (booking_id) REFERENCES booking (id);
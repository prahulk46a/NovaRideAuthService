ALTER TABLE review
    MODIFY booking_id BIGINT NOT NULL;

ALTER TABLE review
    ADD CONSTRAINT uc_review_booking UNIQUE (booking_id);
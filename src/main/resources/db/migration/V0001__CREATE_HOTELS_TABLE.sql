CREATE TABLE hotels
(
    id                SERIAL PRIMARY KEY,
    global_identifier uuid         NOT NULL,
    name              VARCHAR(255) NOT NULL,
    description       TEXT,
    location          VARCHAR(255),
    total_price       BIGINT,
    image_url         VARCHAR(255)
);
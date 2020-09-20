SELECT 'CREATE DATABASE concordance'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'concordance')\gexec

use concordance

CREATE TABLE IF NOT EXISTS book (
    book_id SERIAL PRIMARY KEY ,
    title VARCHAR NOT NULL,
    author VARCHAR NOT NULL,
    translator VARCHAR,
    release_date DATE,
    UNIQUE(title, author),
    CHECK(title <> ''),
    CHECK(author <> '')
);

CREATE TABLE IF NOT EXISTS word (
    word_id SERIAL PRIMARY KEY,
    value VARCHAR NOT NULL UNIQUE,
    length INTEGER DEFAULT 0,
    CHECK(value <> '')
);
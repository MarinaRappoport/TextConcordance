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

CREATE TABLE IF NOT EXISTS word_in_book (
    word_id INTEGER,
    book_id INTEGER,
    index INTEGER NOT NULL,
    line INTEGER NOT NULL,
    index_in_line INTEGER NOT NULL,
    sentence INTEGER NOT NULL,
    paragraph INTEGER NOT NULL,
    PRIMARY KEY(index, book_id, word_id),
    FOREIGN KEY(book_id) REFERENCES book,
    FOREIGN KEY(word_id) REFERENCES word
);
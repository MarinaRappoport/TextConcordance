CREATE TABLE IF NOT EXISTS book (
    book_id SERIAL PRIMARY KEY ,
    title VARCHAR NOT NULL,
    author VARCHAR NOT NULL,
    translator VARCHAR,
    release_date DATE,
    chars_count INTEGER,
    words_count INTEGER,
    sentence_count INTEGER,
    paragraph_count INTEGER,
    UNIQUE(title, author),
    CHECK(title <> ''),
    CHECK(author <> '')
);

CREATE TABLE IF NOT EXISTS word (
    word_id SERIAL PRIMARY KEY,
    value VARCHAR NOT NULL UNIQUE,
    CHECK(value <> '')
);

CREATE TABLE IF NOT EXISTS word_in_book (
    word_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    index INTEGER NOT NULL,
    line INTEGER NOT NULL,
    index_in_line INTEGER NOT NULL,
    sentence INTEGER NOT NULL,
    paragraph INTEGER NOT NULL,
    is_quote_before BOOLEAN,
    is_quote_after BOOLEAN,
    punctuation_mark VARCHAR(5),
    PRIMARY KEY(index, book_id, word_id),
    FOREIGN KEY(book_id) REFERENCES book,
    FOREIGN KEY(word_id) REFERENCES word
);

CREATE TABLE IF NOT EXISTS groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS word_in_group (
    group_id INTEGER NOT NULL,
    word_id INTEGER NOT NULL,
    PRIMARY KEY(group_id, word_id),
    FOREIGN KEY(group_id) REFERENCES groups,
    FOREIGN KEY(word_id) REFERENCES word
);

CREATE TABLE IF NOT EXISTS phrase (
    phrase_id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS word_in_phrase (
    phrase_id INTEGER,
    word_id INTEGER,
    index_in_phrase INTEGER,
    PRIMARY KEY(phrase_id, word_id, index_in_phrase),
    FOREIGN KEY(word_id) REFERENCES word,
    FOREIGN KEY(phrase_id) REFERENCES phrase
);
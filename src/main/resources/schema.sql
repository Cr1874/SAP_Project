-- Rebuilt on every start, so the demo always begins from the same clean data
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id              INTEGER  PRIMARY KEY AUTOINCREMENT,
    username        TEXT     NOT NULL UNIQUE,
    email           TEXT     NOT NULL UNIQUE,
    password_hash   TEXT     NOT NULL,
    role            TEXT     NOT NULL DEFAULT 'USER',
    failed_attempts INTEGER  NOT NULL DEFAULT 0,
    locked_until    DATETIME,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
    id         INTEGER  PRIMARY KEY AUTOINCREMENT,
    user_id    INTEGER  NOT NULL REFERENCES users(id),
    title      TEXT     NOT NULL,
    body       TEXT     NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE audit_log (
    id         INTEGER  PRIMARY KEY AUTOINCREMENT,
    event_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    username   TEXT,
    event_type TEXT     NOT NULL,
    ip_address TEXT,
    details    TEXT
);
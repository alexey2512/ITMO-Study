
CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    level INTEGER NOT NULL
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    level INTEGER NOT NULL,
    access_token VARCHAR(512) UNIQUE NOT NULL,
    registration_date_time TIMESTAMP DEFAULT NOW()
);

CREATE TABLE access_logs (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER REFERENCES employees(id) ON DELETE CASCADE,
    room_id INTEGER REFERENCES rooms(id) ON DELETE CASCADE,
    type VARCHAR(8) NOT NULL CHECK (type IN ('In', 'Out')),
    allowed BOOLEAN NOT NULL,
    date_time TIMESTAMP DEFAULT NOW()
);

CREATE INDEX access_token_index ON employees(access_token);
CREATE INDEX date_time_index ON access_logs(date_time);

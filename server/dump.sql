/* Create tables */
/* Users */
CREATE TABLE IF NOT EXISTS Users (
    id       INTEGER PRIMARY KEY,
    email    TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    name     TEXT,
    surname  TEXT,
    gender   INTEGER DEFAULT 0
);

/* Courses */
CREATE TABLE IF NOT EXISTS Courses (
    id   INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

/* Teachers */
CREATE TABLE IF NOT EXISTS Teachers (
    user_id   INTEGER,
    course_id INTEGER,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id)
        REFERENCES Users (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    FOREIGN KEY (course_id)
        REFERENCES Courses (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

/* Lessons */
CREATE TABLE IF NOT EXISTS Lessons (
    id         INTEGER PRIMARY KEY,
    course_id  INTEGER NOT NULL,
    teacher_id INTEGER NOT NULL,
    unix_day   INTEGER NOT NULL,
    init_hour  INTEGER NOT NULL,
    UNIQUE (course_id, teacher_id),
    FOREIGN KEY (course_id)
        REFERENCES Courses (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id)
        REFERENCES Users (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

/* Bookings */
CREATE TABLE IF NOT EXISTS Bookings (
    user_id   INTEGER,
    lesson_id INTEGER,
    status    TEXT NOT NULL DEFAULT 'RESERVED',
    PRIMARY KEY (user_id, lesson_id),
    FOREIGN KEY (user_id)
        REFERENCES Users (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    FOREIGN KEY (lesson_id)
        REFERENCES Lessons (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

/* --------------- */
/* Populate Tables */
/* Users */
INSERT INTO Users (id, email, password, name, surname, gender)
VALUES
    (1, 'mario.rossi@email.com', 'NOPWD', 'Mario', 'Rossi', 1),
    (2, 'monica.beneventi@email.com', 'NOPWD', 'Monica', 'Beneventi', 2),
    (3, 'anna.panicucci@email.com', 'NOPWD', 'Anna', 'Panicucci', 2),
    (4, 'saverio.bianchi@email.com', 'NOPWD', 'Saverio', 'Bianchi', 1),
    (5, 'remo.moretti@email.com', 'NOPWD', 'Remo', 'Moretti', 1),
    (6, 'tiziana.ferri@email.com', 'NOPWD', 'Tiziana', 'Ferri', 2),
    (7, 'martina.colombo.@email.com', 'NOPWD', 'Martina', ' Colombo.', 2),
    (8, 'luigi.marchese@email.com', 'NOPWD', 'Luigi', 'Marchese', 1),
    (9, 'elisabetta.ferrari@email.com', 'NOPWD', 'Elisabetta', 'Ferrari', 2),
    (10, 'gustavo.costa@email.com', 'NOPWD', 'Gustavo', 'Costa', 1);

/* Courses */
INSERT INTO Courses (id, name)
VALUES
    (1, 'Italiano'),
    (2, 'Matematica'),
    (3, 'Geometria'),
    (4, 'Informatica'),
    (5, 'Storia'),
    (6, 'Geografia'),
    (7, 'Inglese'),
    (8, 'Latino'),
    (9, 'Greco'),
    (10, 'Scienze'),
    (11, 'Filosofia');

/* Teachers */
INSERT INTO Teachers (user_id, course_id)
VALUES
    (1, 7),
    (1, 8),
    (1, 10),
    (2, 1),
    (2, 8),
    (2, 9),
    (3, 1),
    (3, 2),
    (4, 2),
    (4, 3),
    (5, 3),
    (5, 4),
    (6, 4),
    (6, 10),
    (7, 5),
    (7, 6),
    (8, 5),
    (8, 6),
    (9, 9),
    (9, 11),
    (10, 7),
    (10, 11);

/* Lessons */

/* Bookings */


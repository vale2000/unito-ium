#!/usr/bin/python3
import sqlite3


# ------------------------
# SQLite3 Database
# ------------------------
class Database:
    def __init__(self, db_file: str):
        try:
            self.conn = sqlite3.connect(db_file)
            self.init_db()
        except sqlite3.Error as err:
            exit(err)

    def close(self):
        self.conn.close()
        print("Database closed.")

    def init_db(self):
        try:
            c = self.conn.cursor()
            # Users is_teacher se è presente in teachers
            c.executescript("""
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    name TEXT,
                    surname TEXT
                );
                CREATE TABLE IF NOT EXISTS Courses (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL UNIQUE
                );
                CREATE TABLE IF NOT EXISTS Teachers (
                    user_id INTEGER,
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
                CREATE TABLE IF NOT EXISTS Lessons (
                    id INTEGER PRIMARY KEY,
                    course_id  INTEGER NOT NULL,
                    teacher_id INTEGER NOT NULL,
                    data INTEGER NOT NULL,
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
                CREATE TABLE IF NOT EXISTS Bookings (
                    lesson_id INTEGER,
                    user_id   INTEGER,
                    status    TEXT NOT NULL DEFAULT 'TODO',
                    PRIMARY KEY (lesson_id, user_id),
                    FOREIGN KEY (lesson_id)
                        REFERENCES Lessons (id)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                    FOREIGN KEY (user_id)
                        REFERENCES Users (id)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                );
            """)
            self.conn.commit()
        except sqlite3.Error as err:
            exit(err)

#!/usr/bin/python3
from contextlib2 import closing
import sqlite3
import os


# ------------------------
# SQLite3 Database
# ------------------------
class Database:
    def __init__(self, db_path: str):
        try:
            new_db = not os.path.exists(db_path)
            self.conn = sqlite3.connect(db_path)
            if new_db:
                self.init_db()
        except sqlite3.Error as err:
            exit(err)

    def close(self):
        self.conn.close()
        print("Database closed.")

    def add_user(self, email: str, password: str, name: str = '', surname: str = ''):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('INSERT INTO Users (email, password, name, surname) VALUES (?, ?, ?, ?)',
                          [email, password, name, surname])
                return True
        except sqlite3.Error:
            return False

    def get_user(self, user_id: int):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('SELECT * FROM Users WHERE id = ?', [user_id])
                return c.fetchone()
        except sqlite3.Error:
            return ()

    def is_teacher(self, user_id: int):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('SELECT * FROM Users INNER JOIN Teachers ON Users.id = Teachers.id WHERE Users.id = ?',
                          [user_id])
                return c.rowcount() > 0
        except sqlite3.Error:
            return False

    def init_db(self):
        try:
            with closing(self.conn.cursor()) as c:
                c.executescript("""
                    CREATE TABLE IF NOT EXISTS Users (
                        id       INTEGER PRIMARY KEY,
                        email    TEXT NOT NULL UNIQUE,
                        password TEXT NOT NULL,
                        name     TEXT,
                        surname  TEXT
                    );
                    CREATE TABLE IF NOT EXISTS Courses (
                        id   INTEGER PRIMARY KEY,
                        name TEXT NOT NULL UNIQUE
                    );
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
                    CREATE TABLE IF NOT EXISTS Bookings (
                        lesson_id INTEGER,
                        user_id   INTEGER,
                        status    TEXT NOT NULL DEFAULT 'RESERVED',
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
        except sqlite3.IntegrityError as err:
            exit(err)

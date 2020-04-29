#!/usr/bin/python3
from contextlib2 import closing
import sqlite3
import os


# ------------------------
# SQLite3 Database
# ------------------------
class Database:
    def __init__(self, db_path: str, dump_path: str = ''):
        try:
            new_db = not os.path.exists(db_path)
            self.conn = sqlite3.connect(db_path)
            if new_db and dump_path:
                if os.path.exists(dump_path):
                    self.__init_db(dump_path)
                else:
                    print('Dump file not found!')
        except sqlite3.Error as err:
            exit(err)

    def close(self):
        self.conn.close()
        print("Database closed.")

    # ------------------------
    # Users
    # ------------------------
    def add_user(self, email: str, password: str, name: str = '', surname: str = '', gender: int = 0):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('INSERT INTO Users (email, password, name, surname, gender) VALUES (?, ?, ?, ?, ?)',
                          [email, password, name, surname, gender])
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
                c.execute("""SELECT COUNT(*) FROM Users INNER JOIN Teachers 
                                ON Users.id = Teachers.user_id WHERE Users.id = ?""", [user_id])
                return c.fetchone()[0] > 0
        except sqlite3.Error:
            return False

    # ------------------------
    # Courses
    # ------------------------
    def add_course(self, name: str):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('INSERT INTO Courses (name) VALUES (?)', [name])
                return True
        except sqlite3.Error:
            return False

    def get_course(self, course_id: int):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('SELECT * FROM Courses WHERE id = ?', [course_id])
                return c.fetchone()
        except sqlite3.Error:
            return ()

    # ------------------------
    # Lessons
    # ------------------------
    def add_lesson(self, course_id: int, teacher_id: int, unix_day: int, init_hour: int):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('INSERT INTO Lessons (course_id, teacher_id, unix_day, init_hour) VALUES (?, ?, ?, ?)',
                          [course_id, teacher_id, unix_day, init_hour])
                return True
        except sqlite3.Error:
            return False

    def get_lesson(self, lesson_id: int = None, course_id: int = None, teacher_id: int = None):
        try:
            with closing(self.conn.cursor()) as c:
                if lesson_id:
                    c.execute('SELECT * FROM Lessons WHERE id = ?', [lesson_id])
                elif course_id and teacher_id:
                    c.execute('SELECT * FROM Lessons WHERE course_id = ? AND teacher_id = ?', [course_id, teacher_id])
                elif course_id:
                    c.execute('SELECT * FROM Lessons WHERE course_id = ?', [course_id])
                elif teacher_id:
                    c.execute('SELECT * FROM Lessons WHERE teacher_id = ?', [teacher_id])
                return c.fetchone()
        except sqlite3.Error:
            return ()

    # ------------------------
    # Bookings
    # ------------------------
    def add_booking(self, lesson_id: int, user_id: int):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('INSERT INTO Bookings (user_id, lesson_id) VALUES (?, ?)', [user_id, lesson_id])
                return True
        except sqlite3.Error:
            return False

    def get_booking(self, user_id: int, lesson_id: int = None):
        try:
            with closing(self.conn.cursor()) as c:
                if not lesson_id:
                    c.execute("""SELECT * FROM Bookings INNER JOIN Lessons 
                                    ON Lessons.id = Bookings.lesson_id WHERE user_id = ?""", [user_id])
                else:
                    c.execute('SELECT * FROM Bookings WHERE user_id = ? AND lesson_id = ?', [user_id, lesson_id])
                return True
        except sqlite3.Error:
            return False

    # ------------------------
    # Initialize Database
    # ------------------------
    def __init_db(self, dump_path: str):
        try:
            with closing(self.conn.cursor()) as c:
                with open(dump_path, 'r') as f:
                    c.executescript(f.read())
        except sqlite3.IntegrityError as err:
            exit(err)

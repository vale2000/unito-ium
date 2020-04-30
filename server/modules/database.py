#!/usr/bin/python3
import sqlite3
import os


# ------------------------
# SQLite3 Database
# ------------------------
from contextlib import closing


class Database:
    # Singleton Method
    __instance = None

    @staticmethod
    def get_instance(db_path: str = None, dump_path: str = None):
        if not Database.__instance and db_path:
            Database.__instance = Database(db_path, dump_path)
        return Database.__instance

    # ------------------------
    # Class Methods
    def __init__(self, db_path: str, dump_path: str = None):
        try:
            new_db = not os.path.exists(db_path)
            self.conn = sqlite3.connect(db_path, check_same_thread=False)
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

    def get_users(self):
        result = []
        try:
            with closing(self.conn.cursor()) as c:
                c.execute("""WITH IsTeach AS 
                                (SELECT user_id, COUNT(course_id) > 0 AS courses_count FROM Teachers GROUP BY user_id)
                                SELECT id, email, name, surname, IFNULL(IsTeach.courses_count, 0) AS is_teacher, gender
                                FROM Users LEFT JOIN IsTeach ON Users.id = IsTeach.user_id;""")
                rows = c.fetchall()
                for row in rows:
                    result_row = dict(map(lambda x, y: (x[0], y), c.description, row))
                    result.append(result_row)
        except sqlite3.Error:
            pass
        return result

    def get_user(self, user_id: int):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute("""WITH IsTeach AS 
                                (SELECT user_id, COUNT(course_id) > 0 AS courses_count FROM Teachers GROUP BY user_id)
                                SELECT id, email, name, surname, IFNULL(IsTeach.courses_count, 0)AS is_teacher, gender
                                FROM Users LEFT JOIN IsTeach ON IsTeach.user_id = Users.id WHERE Users.id = ?;""",
                          [user_id])
                row = c.fetchone()
                if row:
                    return dict(map(lambda x, y: (x[0], y), c.description, row))
        except sqlite3.Error:
            pass
        return None

    def get_user_login(self, email: str, password: str):
        try:
            with closing(self.conn.cursor()) as c:
                c.execute("""WITH IsTeach AS 
                                (SELECT user_id, COUNT(course_id) > 0 AS courses_count FROM Teachers GROUP BY user_id)
                                SELECT id, IFNULL(IsTeach.courses_count, 0) AS is_teacher
                                FROM Users LEFT JOIN IsTeach ON IsTeach.user_id = Users.id
                                WHERE Users.email = ? AND Users.password = ?;""", [email, password])
                row = c.fetchone()
                if row:
                    return dict(map(lambda x, y: (x[0], y), c.description, row))
        except sqlite3.Error as err:
            print(err)
        return None

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

    def get_courses(self):
        result = []
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('SELECT * FROM Courses;')
                rows = c.fetchall()
                for row in rows:
                    result_row = dict(map(lambda x, y: (x[0], y), c.description, row))
                    result.append(result_row)
        except sqlite3.Error:
            pass
        return result

    def get_course(self, course_id: int):
        result = []
        try:
            with closing(self.conn.cursor()) as c:
                c.execute('SELECT * FROM Courses WHERE id = ?', [course_id])
                row = c.fetchone()
                if row:
                    result_row = dict(map(lambda x, y: (x[0], y), c.description, row))
                    result.append(result_row)
        except sqlite3.Error:
            pass
        return result

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

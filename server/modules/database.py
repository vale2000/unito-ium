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
                c.execute("""SELECT COUNT(*) FROM Users
                                INNER JOIN Teachers 
                                ON Users.id = Teachers.user_id 
                                WHERE Users.id = ?""", [user_id])
                return c.fetchone()[0] > 0
        except sqlite3.Error:
            return False

    def __init_db(self, dump_path: str):
        try:
            with closing(self.conn.cursor()) as c:
                with open(dump_path, 'r') as f:
                    c.executescript(f.read())
        except sqlite3.IntegrityError as err:
            exit(err)

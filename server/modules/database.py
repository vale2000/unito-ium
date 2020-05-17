#!/usr/bin/python3
import os
import sqlite3
import threading


# ------------------------
# SQLite3 Database
# ------------------------
class Database:
    def __init__(self, db_path: str, dump_path: str = None):
        self.lock = threading.Lock()
        with self.lock:
            try:
                new_db = not os.path.exists(db_path)
                self.conn = sqlite3.connect(db_path, check_same_thread=False)
                if new_db and dump_path:
                    if os.path.exists(dump_path):
                        try:
                            with open(dump_path, 'r') as f:
                                self.conn.executescript(f.read())
                                self.conn.commit()
                        except sqlite3.IntegrityError as err:
                            exit(err)
                    else:
                        exit('Dump file not found!')
            except sqlite3.Error as err:
                exit(err)

    def db_lock(self):
        return self.lock # TODO Rimuovere!

    def cursor(self):
        return self.conn.cursor()

    def commit(self):
        self.conn.commit()

    def rollback(self):
        self.conn.rollback()

    def close(self):
        with self.lock:
            self.conn.commit()
            self.conn.close()

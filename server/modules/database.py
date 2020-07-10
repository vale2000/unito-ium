#!/usr/bin/python3
import os
import sqlite3

from modules import config


# ---------------------------
# SQLite3 Database init func
# ---------------------------
def init_db():
    db_path = config.get('database', 'path')
    dump_path = config.get('database', 'dump')
    try:
        new_db = not os.path.exists(db_path)
        with get_db_conn() as database:
            if new_db and dump_path:
                if os.path.exists(dump_path):
                    try:
                        with open(dump_path, 'r') as f:
                            database.executescript(f.read())
                            database.commit()
                    except sqlite3.IntegrityError as err:
                        database.rollback()
                        exit(err)
                else:
                    exit('Dump file not found!')
    except sqlite3.Error as err:
        exit(err)


# -----------------------------
# Return SQLite3 Database conn
# -----------------------------
def get_db_conn(read_only: bool = False):
    conn_str = 'file:' + config.get('database', 'path')
    if read_only:
        conn_str += '?mode=ro'
    return sqlite3.connect(conn_str, uri=True)

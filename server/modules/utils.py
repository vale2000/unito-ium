#!/usr/bin/python3
from flask import request

from modules import simple_jwt
from modules.database import get_db_conn
# ----------------------------------------
# Check Auth Token before execute request
# ----------------------------------------
from server_error import server_error


def logged_before_request():
    try:
        auth = request.headers.get('Authorization')
        if auth is not None:
            token = auth.split(' ')
            if token[0].lower() == 'bearer':
                token_check = simple_jwt.check(token[1])
                if token_check:
                    return
    except UnicodeDecodeError:
        pass
    return server_error('AUTH_FAILED')


# ------------------------
# SQLite3 results to list
# ------------------------
def db_data_to_list(db_data, db_desc):
    db_result = []
    if db_data:
        for row in db_data:
            result_row = dict(map(lambda x, y: (x[0], y), db_desc, row))
            db_result.append(result_row)
    return db_result


# --------------------------------------
# Return permissions of given "role_id"
# --------------------------------------
def get_role_perms(role_id: int):
    if role_id:
        perms = get_role_perms.__cache.get(role_id)
        if perms:
            return perms
        else:
            with get_db_conn(True) as database:
                cursor = database.cursor()
                cursor.execute('SELECT * FROM roles WHERE id = ?', [role_id])
                db_data = cursor.fetchone()
                db_desc = cursor.description
                cursor.close()
            get_role_perms.__cache[role_id] = dict(map(lambda x, y: (x[0], y), db_desc, db_data))
            return get_role_perms.__cache.get(role_id)
    return {}


get_role_perms.__cache = {}  # Static variable

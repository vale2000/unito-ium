#!/usr/bin/python3
import sqlite3

from flask import Blueprint, request, abort, make_response

from modules import simple_jwt
from modules.database import get_db_conn
from server_error import server_error

route_account_public = Blueprint('route_account_public', __name__)


# ------------------------------
# Login by returning user token
# ------------------------------
@route_account_public.route('/account/login', methods=['POST'])
def account_login():
    req_data = request.get_json()
    if req_data:
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT id, role_id FROM users WHERE email = ? AND password = ?""",
                           [req_data.get('email'), req_data.get('password')])
            db_data = cursor.fetchone()
            cursor.close()
        if db_data:
            if db_data[1] == 0 or db_data[1] == 2:  # Deleted and Teachers
                return server_error('LOGIN_DISABLED')
            json_token = {'user': db_data[0], 'role': db_data[1]}
            return make_response({'ok': True, 'data': {'user_id': db_data[0],
                                                       'token': simple_jwt.generate(json_token)}})
        return server_error('LOGIN_FAILED')
    return abort(400)


# ------------------------------
# Register a new user to server
# ------------------------------
@route_account_public.route('/account/register', methods=['POST'])
def account_register():
    req_data = request.get_json()
    result = {'ok': True}
    if req_data:
        with get_db_conn() as database:
            try:
                cursor = database.cursor()
                cursor.execute('INSERT INTO users (email, password) VALUES (?, ?)',
                               [req_data.get('email'), req_data.get('password')])
                database.commit()
            except sqlite3.IntegrityError:
                database.rollback()
                result = server_error('LOGIN_FAILED')
            except sqlite3.Error as e:
                database.rollback()
                result = abort(500)
                print(e)
            finally:
                cursor.close()
        return result
    return abort(400)

#!/usr/bin/python3
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import simple_jwt
from modules.database import get_db_conn

route_account = Blueprint('route_account', __name__)


# ------------------------------
# Login by returning user token
# ------------------------------
@route_account.route('/account/login', methods=['POST'])
def account_login():
    req_data = request.get_json()
    if req_data:
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT users.id, roles.name, role_id FROM users JOIN roles ON roles.id = users.role_id 
                                WHERE email = ? AND password = ?""", [req_data.get('email'), req_data.get('password')])
            db_data = cursor.fetchone()
            cursor.close()
        if db_data:
            if db_data[2] == 0 or db_data[2] == 2:  # TODO Fake and Teachers
                return make_response('{"ok": false, "error": "USER_DISABLED"}', 401)
            json_token = {'user': db_data[0], 'role_name': db_data[1], 'role_id': db_data[2]}
            return make_response({'ok': True, 'token': simple_jwt.generate(json_token)})
        return make_response('{"ok": false, "error": "USER_NOT_FOUND"}', 404)
    abort(400)


# ------------------------------
# Register a new user to server
# ------------------------------
@route_account.route('/account/register', methods=['POST'])
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
                result = make_response({'ok': False, 'error': 'USER_EXIST'}, 500)
            except sqlite3.Error as e:
                database.rollback()
                result = make_response({'ok': False, 'error': str(e)}, 500)
            finally:
                cursor.close()
        return result
    abort(400)

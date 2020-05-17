#!/usr/bin/python3
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import database, simple_jwt
from routes.utils import logged_before_request

route_users = Blueprint('route_users', __name__)
route_users.before_request(logged_before_request)


# --------------------------
# Return users list
# --------------------------
@route_users.route('/users', methods=['GET'])
def user_list():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    with database.db_lock():
        cursor = database.cursor()
        cursor.execute("""SELECT users.id, email, users.name, surname, role_id, gender FROM users
                            JOIN roles ON roles.user_list = 1 WHERE roles.id = ?""", [token_data.get('role')])
        db_data = cursor.fetchall()
        db_desc = cursor.description
        cursor.close()
        if db_data:
            result = []
            for row in db_data:
                result_row = dict(map(lambda x, y: (x[0], y), db_desc, row))
                result.append(result_row)
            return {'ok': True, 'data': result}
        abort(401)


# --------------------------
# Create new user
# --------------------------
@route_users.route('/users', methods=['POST'])
def user_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    req_data = request.get_json()
    result = {'ok': True}
    if req_data:
        if token_data.get('role') == 3:
            req_data.remove('id')
            if req_data.get('password') is None:
                req_data.set('password', 'FAKE_USER')
                req_data.set('role_id', 0)
            sql_str = 'INSERT INTO users (' \
                      + (', '.join(req_data.keys())) + ') VALUES (' \
                      + (', '.join('?' for x in req_data.keys())) + ')'
            with database.db_lock():
                try:
                    cursor = database.cursor()
                    cursor.execute(sql_str, req_data.values())
                    database.commit()
                except sqlite3.IntegrityError:
                    database.rollback()
                    result = {'ok': False, 'error': 'USER_EXIST'}
                finally:
                    cursor.close()
            return result
        abort(401)
    abort(400)


# --------------------------
# Return user <id> data
# --------------------------
@route_users.route('/users/<int:user_id>', methods=['GET'])
def user_get(user_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    if token_data.get('role') == 3 or token_data.get('user') == user_id:
        with database.db_lock():
            cursor = database.cursor()
            cursor.execute('SELECT id, email, name, surname, role_id, gender FROM users WHERE id = ?', [user_id])
            db_data = cursor.fetchone()
            db_desc = cursor.description
            cursor.close()
        if db_data:
            row = dict(map(lambda x, y: (x[0], y), db_desc, db_data))
            return {'ok': True, 'data': row}
        else:
            return {'ok': False, 'error': 'USER_NOT_FOUND'}, 404
    abort(401)


# --------------------------
#
# --------------------------
@route_users.route('/users/<int:user_id>', methods=['PUT'])
def user_update(user_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    req_data = request.get_json()
    if req_data:
        if token_data['role'] == 3 or token_data['user'] == user_id:
            req_data.remove('id')
            sql_str = 'UPDATE users SET ' + (', '.join("%s = ?" % v for v in req_data.keys())) + ' WHERE id = ?'
            with database.db_lock():
                try:
                    cursor = database.cursor()
                    cursor.execute(sql_str, req_data.values() + [user_id])
                    database.commit()
                    result = make_response({'ok': True}, 200)
                except sqlite3.Error as e:
                    database.rollback()
                    result = make_response({'ok': False, 'error': 'USER_NOT_FOUND'}, 404)
                    print(e)
                finally:
                    cursor.close()
            return result
        abort(401)
    abort(400)


# --------------------------
#
# --------------------------
@route_users.route('/users/<int:user_id>', methods=['DELETE'])
def user_remove(user_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    if token_data['role'] == 3 or token_data['user'] == user_id:
        with database.db_lock():
            try:
                cursor = database.cursor()
                cursor.execute('DELETE FROM users WHERE id = ?', [user_id])
                database.commit()
                result = make_response({'ok': True}, 200)
            except sqlite3.Error as e:
                database.rollback()
                result = make_response({'ok': False, 'error': 'USER_NOT_FOUND'}, 404)
                print(e)
            finally:
                cursor.close()
        return result
    abort(401)

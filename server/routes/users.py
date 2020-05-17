#!/usr/bin/python3
import itertools
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request, get_role_perms

route_users = Blueprint('route_users', __name__)
route_users.before_request(logged_before_request)


# --------------------------
# Return users list
# --------------------------
@route_users.route('/users', methods=['GET'])
def user_list():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('user_list', 0):
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT users.id, users.email, users.role_id, users.name, users.surname, users.gender,
                                courses.id, courses.name FROM users
                                LEFT JOIN teachers ON teachers.user_id = users.id
                                LEFT JOIN courses ON courses.id = teachers.course_id ORDER BY users.id""")
            db_data = cursor.fetchall()
            cursor.close()
        db_results = []
        if db_data:  # TODO Migliorare/Rimuovere
            for key, rows in itertools.groupby(db_data, key=lambda x: x[0]):
                courses = []
                row = None
                for r in rows:
                    if row is None:
                        row = r
                    if r[6] is not None and r[7] is not None:
                        courses.append({'id': r[6], 'name': r[7]})
                db_results.append({'id': row[0], 'email': row[1], 'role_id': row[2],  'name': row[3],
                                   'surname': row[4], 'gender': row[5], 'courses': courses})
        return make_response({'ok': True, 'data': db_results}, 200)
    abort(401)


# --------------------------
# Create new user
# --------------------------
@route_users.route('/users', methods=['POST'])
def user_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('user_add', 0):
            req_data.remove('id')
            if req_data.get('password') is None:
                req_data.set('password', 'FAKE_USER')
                req_data.set('role_id', 0)
            sql_str = 'INSERT INTO users (' \
                      + (', '.join(req_data.keys())) + ') VALUES (' + (', '.join('?' for x in req_data.keys())) + ')'
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute(sql_str, req_data.values())
                    last_id_inserted = cursor.lastrowid
                    database.commit()
                    result = make_response({'ok': True, 'data': last_id_inserted}, 200)
                except sqlite3.Error as e:
                    database.rollback()
                    result = make_response({'ok': False, 'error': str(e)}, 500)
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
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('user_get', 0) or user_perms.get('user_get_others', 0):
        if not user_perms.get('user_get_others', 0):
            if user_id != token_data.get('user'):
                return abort(401)
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT users.id, users.email, users.role_id, users.name, users.surname, users.gender,
                                courses.id, courses.name FROM users
                                LEFT JOIN teachers ON teachers.user_id = users.id
                                LEFT JOIN courses ON courses.id = teachers.course_id
                                WHERE users.id = ? ORDER BY users.id""", [user_id])
            db_data = cursor.fetchall()
            cursor.close()
        if db_data:
            courses = []
            for key, rows in itertools.groupby(db_data, key=lambda x: x[0]):
                for r in rows:
                    if r[6] is not None and r[7] is not None:
                        courses.append({'id': r[6], 'name': r[7]})
            db_result = {'id': db_data[0][0], 'email': db_data[0][1], 'role_id': db_data[0][2],
                         'name': db_data[0][3], 'surname': db_data[0][4], 'courses': courses}
            return make_response({'ok': True, 'data': db_result}, 200)
        else:
            return make_response({'ok': False, 'error': 'USER_NOT_FOUND'}, 404)


# --------------------------
#
# --------------------------
@route_users.route('/users/<int:user_id>', methods=['PUT'])
def user_update(user_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('user_get', 0) or user_perms.get('user_get_others', 0):
            if not user_perms.get('user_get_others', 0):
                if user_id != token_data.get('user'):
                    return abort(401)
            req_data.remove('id')
            sql_str = 'UPDATE users SET ' + (', '.join("%s = ?" % v for v in req_data.keys())) + ' WHERE id = ?'
            with get_db_conn() as database:
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
    abort(400)


# --------------------------
#
# --------------------------
@route_users.route('/users/<int:user_id>', methods=['DELETE'])
def user_remove(user_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('user_delete', 0):
        with get_db_conn() as database:
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

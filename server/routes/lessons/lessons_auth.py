#!/usr/bin/python3
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request, get_role_perms

route_lessons_auth = Blueprint('route_lessons_auth', __name__)
route_lessons_auth.before_request(logged_before_request)  # Check for login


# --------------------------
# Create new lesson
# --------------------------
@route_lessons_auth.route('/lessons', methods=['POST'])
def lesson_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('lesson_add', 0):
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute("""INSERT INTO lessons (course_id, unix_day, init_hour) VALUES (?, ?, ?)""",
                                   [req_data.get('course_id'), req_data.get('unix_day'), req_data.get('init_hour')])
                    last_id_inserted = cursor.lastrowid
                    database.commit()
                    result = make_response({'ok': True, 'data': last_id_inserted}, 200)
                except sqlite3.Error as e:
                    database.rollback()
                    result = make_response({'ok': False, 'error': str(e)}, 500)
                finally:
                    cursor.close()
            return result

        return abort(401)
    return abort(400)


# --------------------------
#
# --------------------------
@route_lessons_auth.route('/lessons/<int:lesson_id>', methods=['PUT'])
def lesson_update(lesson_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('lesson_update', 0):
            req_data.pop('id', None)
            sql_str = 'UPDATE lessons SET ' + (', '.join(f'{v} = ?' for v in req_data.keys())) + ' WHERE id = ?'
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute(sql_str, list(req_data.values()) + [lesson_id])
                    database.commit()
                    result = make_response({'ok': True}, 200)
                except sqlite3.Error:
                    database.rollback()
                    result = make_response({'ok': False, 'error': 'LESSON_NOT_FOUND'}, 404)
                finally:
                    cursor.close()
            return result
        return abort(401)
    return abort(400)


# --------------------------
#
# --------------------------
@route_lessons_auth.route('/lessons/<int:lesson_id>', methods=['DELETE'])
def lesson_remove(lesson_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('lesson_delete', 0):
        with get_db_conn() as database:
            try:
                cursor = database.cursor()
                cursor.execute('DELETE FROM lessons WHERE id = ?', [lesson_id])
                database.commit()
                result = make_response({'ok': True}, 200)
            except sqlite3.Error:
                database.rollback()
                result = make_response({'ok': False, 'error': 'LESSON_NOT_FOUND'}, 404)
            finally:
                cursor.close()
        return result
    return abort(401)

#!/usr/bin/python3
import sqlite3

from flask import Blueprint, request, abort, make_response

from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request, get_role_perms
from server_error import server_error

route_courses_auth = Blueprint('route_courses_auth', __name__)
route_courses_auth.before_request(logged_before_request)  # Check for login


# --------------------------
# Create new course
# --------------------------
@route_courses_auth.route('/courses', methods=['POST'])
def course_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('user'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('course_add', 0):
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute("""INSERT INTO courses (name) VALUES (?)""", [req_data.get('name')])
                    last_id_inserted = cursor.lastrowid
                    database.commit()
                    result = make_response({'ok': True, 'data': last_id_inserted}, 200)
                except sqlite3.Error as e:
                    database.rollback()
                    result = abort(500)
                    print(e)
                finally:
                    cursor.close()
            return result

        return abort(401)
    return abort(400)


# --------------------------
#
# --------------------------
@route_courses_auth.route('/courses/<int:course_id>', methods=['PUT'])
def course_update(course_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('course_update', 0):
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute('UPDATE courses SET name = ? WHERE id = ?', [req_data.get('name'), course_id])
                    database.commit()
                    result = make_response({'ok': True}, 200)
                except sqlite3.Error:
                    database.rollback()
                    result = server_error('COURSE_NOT_FOUND')
                finally:
                    cursor.close()
            return result
        return abort(401)
    return abort(400)


# --------------------------
#
# --------------------------
@route_courses_auth.route('/courses/<int:course_id>', methods=['DELETE'])
def course_remove(course_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role'))
    if user_perms.get('course_delete', 0):
        with get_db_conn() as database:
            try:
                cursor = database.cursor()
                cursor.execute('DELETE FROM courses WHERE id = ?', [course_id])
                database.commit()
                result = make_response({'ok': True}, 200)
            except sqlite3.Error:
                database.rollback()
                result = server_error('COURSE_NOT_FOUND')
            finally:
                cursor.close()
        return result
    return abort(401)

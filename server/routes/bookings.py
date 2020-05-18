#!/usr/bin/python3
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request, get_role_perms


route_bookings = Blueprint('route_bookings', __name__)
route_bookings.before_request(logged_before_request)  # Check for login


# --------------------------
# Return bookings list
# --------------------------
@route_bookings.route('/bookings', methods=['GET'])
def booking_list():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('booking_list', 0) or user_perms.get('booking_list_others', 0):
        user_id = token_data.get('user')
        if user_perms.get('booking_list_others', 0):
            req_data = request.get_json()
            if req_data:
                user_id = req_data.get('user_id', token_data.get('user'))
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT bookings.id, bookings.status, lessons.id, lessons.unix_day, lessons.init_hour,
                                lessons.course_id, courses.name, lessons.teacher_id, users.name, users.surname 
                                FROM bookings JOIN lessons ON lessons.id = bookings.lesson_id
                                JOIN courses ON courses.id = lessons.course_id
                                JOIN users ON users.id = lessons.teacher_id
                                WHERE bookings.user_id = ?""", [user_id])
            db_data = cursor.fetchall()
            cursor.close()
            db_result = []
            if db_data:
                for row in db_data:
                    course = {'id': row[5], 'name': row[6]}
                    teacher = {'id': row[7], 'name': row[8], 'surname': row[9]}
                    lesson = {'id': row[2], 'unix_day': row[3], 'init_hour': row[4],
                              'course': course, 'teacher': teacher}
                    db_result.append({'id': row[0], 'status': row[1], 'lesson': lesson})
        return make_response({'ok': True, 'data': db_result}, 200)
    return abort(401)


# --------------------------
# Create new booking
# --------------------------
@route_bookings.route('/bookings', methods=['POST'])
def booking_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('booking_add', 0) or user_perms.get('booking_add_other', 0):
            user_id = token_data.get('user')
            if user_perms.get('booking_add_other', 0) and req_data.get('user_id') is not None:
                user_id = req_data.get('user_id')
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute('INSERT INTO bookings (user_id, lesson_id) VALUES (?, ?)',
                                   [user_id, req_data.get('lesson_id')])
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
# Return booking <id> data
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['GET'])
def booking_get(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('booking_get', 0) or user_perms.get('booking_get_others', 0):
        user_id = token_data.get('user')
        if user_perms.get('booking_get_others', 0):
            req_data = request.get_json()
            if req_data:
                user_id = req_data.get('user_id', token_data.get('user'))
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT bookings.id, bookings.status, lessons.id, lessons.unix_day, lessons.init_hour,
                                lessons.course_id, courses.name, lessons.teacher_id, users.name, users.surname 
                                FROM bookings JOIN lessons ON lessons.id = bookings.lesson_id
                                JOIN courses ON courses.id = lessons.course_id
                                JOIN users ON users.id = lessons.teacher_id
                                WHERE bookings.id = ? AND bookings.user_id = ?""", [booking_id, user_id])
            db_data = cursor.fetchone()
            cursor.close()
        if db_data:
            course = {'id': db_data[5], 'name': db_data[6]}
            teacher = {'id': db_data[7], 'name': db_data[8], 'surname': db_data[9]}
            lesson = {'id': db_data[2], 'unix_day': db_data[3], 'init_hour': db_data[4],
                      'course': course, 'teacher': teacher}
            return make_response({'id': db_data[0], 'status': db_data[1], 'lesson': lesson}, 200)
        return make_response({'ok': False, 'error': 'BOOKING_NOT_FOUND'}, 404)
    return abort(401)


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['PUT'])
def booking_update(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('booking_update', 0) or user_perms.get('booking_update_others', 0):
            req_data.remove('id')
            user_id = req_data.get('user_id', token_data.get('user'))
            if not user_perms.get('booking_update_others', 0):
                user_id = token_data.get('user')
                req_data.remove('user_id')
                req_data.remove('lesson_id')
            sql_str = 'UPDATE bookings SET ' + (', '.join("%s = ?" % v for v in req_data.keys())) \
                      + ' WHERE id = ? AND user_id = ?'
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute(sql_str, req_data.values() + [booking_id, user_id])
                    database.commit()
                    result = make_response({'ok': True}, 200)
                except sqlite3.Error:
                    database.rollback()
                    result = make_response({'ok': False, 'error': 'BOOKING_NOT_FOUND'}, 404)
                finally:
                    cursor.close()
            return result
        return abort(401)
    return abort(400)


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['DELETE'])
def booking_remove(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role_id'))
    if user_perms.get('booking_delete', 0) or user_perms.get('booking_delete_others', 0):
        user_id = token_data.get('user')
        if user_perms.get('booking_delete_others', 0):
            req_data = request.get_json()
            if req_data:
                user_id = req_data.get('user_id', token_data.get('user'))
        with get_db_conn() as database:
            try:
                cursor = database.cursor()
                cursor.execute('DELETE FROM bookings WHERE id = ? AND user_id = ?', [booking_id, user_id])
                database.commit()
                result = make_response({'ok': True}, 200)
            except sqlite3.Error:
                database.rollback()
                result = make_response({'ok': False, 'error': 'BOOKING_NOT_FOUND'}, 404)
            finally:
                cursor.close()
        return result
    return abort(401)

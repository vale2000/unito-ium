#!/usr/bin/python3
import sqlite3

from flask import Blueprint, request, abort, make_response

from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request, get_role_perms
from server_error import server_error

route_bookings = Blueprint('route_bookings', __name__)
route_bookings.before_request(logged_before_request)  # Check for login


# --------------------------
# Return bookings list
# --------------------------
@route_bookings.route('/bookings', methods=['GET'])
def booking_list():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role'))
    if user_perms.get('booking_list', 0) or user_perms.get('booking_list_others', 0):
        user_id = token_data.get('user')
        if user_perms.get('booking_list_others', 0):
            req_data = request.get_json()
            if req_data:
                user_id = req_data.get('user_id', token_data.get('user'))
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT bookings.id, bookings.status, bookings.day, bookings.hour, bookings.course_id, 
                            bookings.course_name, bookings.teacher_id, bookings.teacher_name, bookings.teacher_surname
                                FROM bookings WHERE bookings.user_id = ?
                                ORDER BY bookings.day DESC, bookings.hour DESC""", [user_id])
            db_data = cursor.fetchall()
            cursor.close()
            db_results = []
            if db_data:
                for row in db_data:
                    course = {'id': row[4], 'name': row[5]}
                    teacher = {'id': row[6], 'name': row[7], 'surname': row[8]}
                    lesson = {'day': row[2], 'hour': row[3], 'course': course, 'teachers': [teacher]}
                    db_results.append({'id': row[0], 'status': row[1], 'lesson': lesson})
        return make_response({'ok': True, 'data': db_results}, 200)
    return abort(401)


# --------------------------
# Create new booking
# --------------------------
@route_bookings.route('/bookings', methods=['POST'])
def booking_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('booking_add', 0) or user_perms.get('booking_add_other', 0):
            user_id = token_data.get('user')
            if user_perms.get('booking_add_other', 0):
                user_id = req_data.get('user_id', token_data.get('user'))
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    for hour in req_data.get('hours'):
                        cursor.execute("""INSERT INTO bookings (user_id, course_id, course_name, teacher_id, 
                                                                teacher_name, teacher_surname, day, hour) 
                                            SELECT ?, c.id, c.name, t.id, t.name, t.surname, ?, ? FROM users AS t 
                                            JOIN courses AS c ON c.id = ? WHERE t.id = ? AND
                                            NOT EXISTS(SELECT b2.id FROM bookings as b2 WHERE b2.teacher_id = t.id 
                                            AND b2.day = ? AND b2.hour = ? 
                                            AND (b2.status = 'DONE' OR b2.status = 'RESERVED'))""",
                                       [user_id, req_data.get('day'), hour, req_data.get('course_id'),
                                        req_data.get('teacher_id'), req_data.get('day'), hour])
                    last_id_inserted = cursor.lastrowid
                    database.commit()

                    if last_id_inserted > 0:
                        result = make_response({'ok': True}, 200)
                    else:
                        result = abort(400)
                except sqlite3.Error as e:
                    print(e)
                    database.rollback()
                    result = abort(500)
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
    user_perms = get_role_perms(token_data.get('role'))
    if user_perms.get('booking_get', 0) or user_perms.get('booking_get_others', 0):
        user_id = token_data.get('user')
        if user_perms.get('booking_get_others', 0):
            req_data = request.get_json()
            if req_data:
                user_id = req_data.get('user_id', token_data.get('user'))
        with get_db_conn(True) as database:
            cursor = database.cursor()
            cursor.execute("""SELECT bookings.id, bookings.status, bookings.day, bookings.hour, bookings.course_id, 
                                bookings.course_name, bookings.teacher_id, bookings.teacher_name, 
                                bookings.teacher_surname FROM bookings
                                WHERE bookings.id = ? AND bookings.user_id = ?""", [booking_id, user_id])
            db_data = cursor.fetchone()
            cursor.close()
        if db_data:
            course = {'id': db_data[4], 'name': db_data[5]}
            teacher = {'id': db_data[6], 'name': db_data[7], 'surname': db_data[8]}
            lesson = {'day': db_data[2], 'hour': db_data[3], 'course': course, 'teachers': [teacher]}
            return make_response({'ok': True, 'data': {'id': db_data[0], 'status': db_data[1], 'lesson': lesson}}, 200)
        return server_error('BOOKING_NOT_FOUND')
    return abort(401)


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['PUT'])
def booking_update(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_perms = get_role_perms(token_data.get('role'))
    req_data = request.get_json()
    if req_data:
        if user_perms.get('booking_update', 0) or user_perms.get('booking_update_others', 0):
            if user_perms.get('booking_update', 0) and not user_perms.get('booking_update_others', 0):
                if req_data.get('status') not in ["DONE", "CANCELED"]:
                    return server_error('UNAUTHORIZED')
            req_data.pop('id', None)
            user_id = req_data.get('user_id', token_data.get('user'))
            if not user_perms.get('booking_update_others', 0):
                user_id = token_data.get('user')
                req_data.pop('user_id', None)
                req_data.pop('teacher_id', None)
                req_data.pop('lesson_id', None)
            sql_str = 'UPDATE bookings SET ' + (', '.join(f'{v} = ?' for v in req_data.keys())) \
                      + ' WHERE id = ? AND user_id = ?'
            with get_db_conn() as database:
                try:
                    cursor = database.cursor()
                    cursor.execute(sql_str, list(req_data.values()) + [booking_id, user_id])
                    database.commit()
                    result = make_response({'ok': True}, 200)
                except sqlite3.Error:
                    database.rollback()
                    result = server_error('BOOKING_NOT_FOUND')
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
    user_perms = get_role_perms(token_data.get('role'))
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
                result = server_error('BOOKING_NOT_FOUND')
            finally:
                cursor.close()
        return result
    return abort(401)

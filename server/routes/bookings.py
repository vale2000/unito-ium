#!/usr/bin/python3
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request, get_role_perms, db_data_to_list


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
            cursor.execute('SELECT id, user_id, lesson_id, status FROM bookings WHERE user_id = ?', [user_id])
            db_data = cursor.fetchall()
            db_desc = cursor.description
            cursor.close()
        db_result = db_data_to_list(db_data, db_desc)
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
            cursor.execute('SELECT * FROM bookings WHERE id = ? AND user_id = ?', [booking_id, user_id])
            db_data = cursor.fetchone()
            db_desc = cursor.description
            cursor.close()
        if db_data:
            row = dict(map(lambda x, y: (x[0], y), db_desc, db_data))
            return make_response({'ok': True, 'data': row}, 200)
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

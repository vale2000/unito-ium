#!/usr/bin/python3
import sqlite3
from flask import Blueprint, request, abort, make_response
from modules import database, simple_jwt
from routes.utils import logged_before_request, get_user_id

route_bookings = Blueprint('route_bookings', __name__)
route_bookings.before_request(logged_before_request)  # Check for login


# --------------------------
# Return bookings list
# --------------------------
@route_bookings.route('/bookings', methods=['GET'])
def booking_list():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_id = get_user_id(token_data, request.get_json())
    with database.db_lock():
        try:
            cursor = database.cursor()
            cursor.execute('SELECT id, user_id, lesson_id, status FROM bookings WHERE user_id = ?', [user_id])
            db_data = cursor.fetchall()
            db_result = []
            for row in db_data:
                result_row = dict(map(lambda x, y: (x[0], y), cursor.description, row))
                db_result.append(result_row)
            result = make_response({'ok': True, 'data': db_result}, 200)
        except sqlite3.Error as e:
            result = make_response({'ok': False, 'error': str(e)}, 500)
            print(e)
        finally:
            cursor.close()
    return result


# --------------------------
# Create new booking
# --------------------------
@route_bookings.route('/bookings', methods=['POST'])
def booking_add():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    req_data = request.get_json()
    if req_data:
        if token_data.get('role') != 3 or req_data.get('user_id') is None:
            req_data.set('user_id', token_data.get('user'))
        with database.db_lock():
            try:
                cursor = database.cursor()
                cursor.execute('INSERT INTO bookings (user_id, lesson_id) VALUES (?, ?)',
                               [req_data.get('user_id'), req_data.get('lesson_id')])
                database.commit()
                result = make_response({'ok': True}, 200)
            except sqlite3.Error as e:
                database.rollback()
                result = make_response({'ok': False, 'error': str(e)}, 500)
                print(e)
            finally:
                cursor.close()
        return result
    abort(400)


# --------------------------
# Return booking <id> data
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['GET'])
def booking_get(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_id = get_user_id(token_data, request.get_json())
    with database.db_lock():
        cursor = database.cursor()
        cursor.execute('SELECT user_id, lesson_id, status FROM bookings WHERE id = ? AND user_id = ?',
                       [booking_id, user_id])
        db_data = cursor.fetchone()
        db_desc = cursor.description
        cursor.close()
    if db_data:
        row = dict(map(lambda x, y: (x[0], y), db_desc, db_data))
        return make_response({'ok': True, 'data': row}, 200)
    return make_response({'ok': False, 'error': 'BOOKING_NOT_FOUND'}, 404)


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['PUT'])
def booking_update(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    req_data = request.get_json()
    if req_data:
        req_data.remove('id')
        user_id = get_user_id(token_data, req_data)
        if token_data.get('role') != 3:
            req_data.remove('user_id')
            req_data.remove('lesson_id')
        sql_str = 'UPDATE users SET ' + (', '.join("%s = ?" % v for v in req_data.keys())) \
                  + ' WHERE id = ? AND user_id = ?'
        with database.db_lock():
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
    abort(400)


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:booking_id>', methods=['DELETE'])
def booking_remove(booking_id: int):
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_id = get_user_id(token_data, request.get_json())
    with database.db_lock():
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

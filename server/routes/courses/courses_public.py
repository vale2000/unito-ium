#!/usr/bin/python3
from flask import Blueprint, make_response
from modules.database import get_db_conn
from modules.utils import db_data_to_list


route_courses_public = Blueprint('route_courses_public', __name__)


# --------------------------
# Return courses list
# --------------------------
@route_courses_public.route('/courses', methods=['GET'])
def course_list():
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute('SELECT * FROM courses')
        db_data = cursor.fetchall()
        db_desc = cursor.description
        cursor.close()
    db_result = db_data_to_list(db_data, db_desc)
    return make_response({'ok': True, 'data': db_result}, 200)


# --------------------------
# Return course <id> data
# --------------------------
@route_courses_public.route('/courses/<int:course_id>', methods=['GET'])
def course_get(course_id: int):
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute('SELECT * FROM courses WHERE id = ?', [course_id])  # TODO show teachers id/name
        db_data = cursor.fetchall()
        db_desc = cursor.description
        cursor.close()
    if db_data:
        row = dict(map(lambda x, y: (x[0], y), db_desc, db_data))
        return make_response({'ok': True, 'data': row}, 200)
    return make_response({'ok': False, 'error': 'COURSE_NOT_FOUND'}, 404)

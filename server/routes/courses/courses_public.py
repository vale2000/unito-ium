#!/usr/bin/python3
import itertools

from flask import Blueprint, make_response

from modules.database import get_db_conn
from modules.utils import db_data_to_list
from server_error import server_error

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
        cursor.execute("""SELECT courses.id, courses.name, users.id, users.name, users.surname FROM courses
                            LEFT JOIN teachers ON teachers.course_id = courses.id
                            LEFT JOIN users ON users.id = teachers.user_id
                            WHERE courses.id = ? ORDER BY courses.id""", [course_id])
        db_data = cursor.fetchall()
        cursor.close()
    if db_data:
        users = []
        for key, rows in itertools.groupby(db_data, key=lambda x: x[0]):
            for r in rows:
                if r[2] is not None:
                    users.append({'id': r[2], 'name': r[3], 'surname': r[4]})
        db_result = {'id': db_data[0][0], 'name': db_data[0][1], 'teachers': users}
        return make_response({'ok': True, 'data': db_result}, 200)
    return server_error('COURSE_NOT_FOUND')

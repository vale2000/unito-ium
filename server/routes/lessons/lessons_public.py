#!/usr/bin/python3
from flask import Blueprint, make_response
from modules.database import get_db_conn
from modules.utils import db_data_to_list


route_lessons_public = Blueprint('route_lessons_public', __name__)


# --------------------------
# Return lessons list
# --------------------------
@route_lessons_public.route('/lessons', methods=['GET'])
def lesson_list():
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute('SELECT * FROM lessons')
        db_data = cursor.fetchall()
        db_desc = cursor.description
        cursor.close()
    db_result = db_data_to_list(db_data, db_desc)
    return make_response({'ok': True, 'data': db_result}, 200)


# --------------------------
# Return lesson <id> data
# --------------------------
@route_lessons_public.route('/lessons/<int:lesson_id>', methods=['GET'])
def lesson_get(lesson_id: int):
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute('SELECT * FROM lessons WHERE id = ?', [lesson_id])
        db_data = cursor.fetchall()
        db_desc = cursor.description
        cursor.close()
    if db_data:
        row = dict(map(lambda x, y: (x[0], y), db_desc, db_data))
        return make_response({'ok': True, 'data': row}, 200)
    return make_response({'ok': False, 'error': 'LESSON_NOT_FOUND'}, 404)

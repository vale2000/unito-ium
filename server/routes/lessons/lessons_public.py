#!/usr/bin/python3
import itertools
from flask import Blueprint, make_response, request
from modules.database import get_db_conn
route_lessons_public = Blueprint('route_lessons_public', __name__)


# --------------------------
# Return lessons list
# --------------------------
@route_lessons_public.route('/lessons', methods=['GET'])
def lesson_list():
    if 'legacy' in request.args:
        sql_order = 'lessons.unix_day, lessons.init_hour'
    else:
        sql_order = 'lessons.course_id, lessons.unix_day, lessons.init_hour'
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, lessons.course_id, courses.name, 
                            lessons.teacher_id, users.name, users.surname FROM lessons 
                            JOIN courses ON courses.id = lessons.course_id
                            JOIN users ON users.id = lessons.teacher_id
                            WHERE NOT EXISTS (SELECT * FROM bookings WHERE bookings.status != "CANCELED" 
                            AND bookings.lesson_id = lessons.id) ORDER BY """ + sql_order)
        db_data = cursor.fetchall()
        cursor.close()
    db_results = []
    if db_data:
        if 'legacy' in request.args:
            for row in db_data:
                course = {'id': row[3], 'name': row[4]}
                teacher = {'id': row[5], 'name': row[6], 'surname': row[7]}
                lesson = {'id': row[0], 'unix_day': row[1], 'init_hour': row[2], 'teacher': teacher}
                db_results.append({'course': course, 'lesson': lesson})
        else:
            for key, rows in itertools.groupby(db_data, key=lambda x: x[3]):
                static_col = None
                lessons = []
                for r in rows:
                    if static_col is None:
                        static_col = r
                    lessons.append({'id': r[0], 'unix_day': r[1], 'init_hour': r[2],
                                    'teacher': {'id': r[5], 'name': r[6], 'surname': r[7]}})
                db_results.append({'course': {'id': static_col[3], 'name': static_col[4]}, 'lessons': lessons})
    return make_response({'ok': True, 'data': db_results}, 200)


# --------------------------
# Return lesson <id> data
# --------------------------
@route_lessons_public.route('/lessons/<int:lesson_id>', methods=['GET'])
def lesson_get(lesson_id: int):
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, lessons.course_id, courses.name, 
                            lessons.teacher_id, users.name, users.surname FROM lessons 
                            JOIN courses ON courses.id = lessons.course_id
                            JOIN users ON users.id = lessons.teacher_id WHERE lessons.id = ?""", [lesson_id])
        db_data = cursor.fetchone()
        cursor.close()
    if db_data:
        teacher = {'id': db_data[5], 'name': db_data[6], 'surname': db_data[7]}
        course = {'id': db_data[3], 'name': db_data[4]}
        return make_response({'ok': True, 'data': {'id': db_data[0], 'unix_day': db_data[1], 'init_hour': db_data[2],
                                                   'course': course, 'teacher': teacher}}, 200)
    return make_response({'ok': False, 'error': 'LESSON_NOT_FOUND'}, 404)

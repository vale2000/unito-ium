#!/usr/bin/python3
from flask import Blueprint, make_response
from modules.database import get_db_conn


route_lessons_public = Blueprint('route_lessons_public', __name__)


# --------------------------
# Return lessons list
# --------------------------
@route_lessons_public.route('/lessons', methods=['GET'])
def lesson_list():
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, lessons.course_id, courses.name, 
                            lessons.teacher_id, users.name, users.surname FROM lessons 
                            JOIN courses ON courses.id = lessons.course_id
                            JOIN users ON users.id = lessons.teacher_id
                            ORDER BY lessons.unix_day, lessons.init_hour""")
        db_data = cursor.fetchall()
        cursor.close()
        db_result = []
        if db_data:
            for row in db_data:
                course = {'id': row[3], 'name': row[4]}
                teacher = {'id': row[5], 'name': row[6], 'surname': row[7]}
                db_result.append({'id': row[0], 'unix_day': row[1], 'init_hour': row[2], 'course': course,
                                  'teacher': teacher})
    return make_response({'ok': True, 'data': db_result}, 200)


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

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
    with get_db_conn(True) as database:  # TODO Rimuovere i professori e aggiungere lista lezioni
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, courses.id, courses.name,
                            users.id, users.name, users.surname FROM lessons
                            JOIN courses ON courses.id = lessons.course_id
                            LEFT JOIN teachers ON teachers.course_id = lessons.course_id
                            LEFT JOIN users ON users.id = teachers.user_id
                            WHERE NOT EXISTS (SELECT * FROM bookings WHERE bookings.status != 'CANCELED'
                            AND bookings.lesson_id = lessons.id AND bookings.teacher_id = teachers.user_id)
                            ORDER BY lessons.course_id, lessons.unix_day, lessons.init_hour""")
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
                stat_col = None
                teachers = []
                for r in rows:
                    if stat_col is None:
                        stat_col = r
                    teachers.append({'id': r[5], 'name': r[6], 'surname': r[7]})
                lesson = {'id': stat_col[0], 'unix_day': stat_col[1], 'init_hour': stat_col[2], 'teachers': teachers}
                db_results.append({'course': {'id': stat_col[3], 'name': stat_col[4]}, 'lesson': lesson})
    return make_response({'ok': True, 'data': db_results}, 200)


# --------------------------
# Return lesson <id> data
# --------------------------
@route_lessons_public.route('/lessons/<int:lesson_id>', methods=['GET'])
def lesson_get(lesson_id: int):
    with get_db_conn(True) as database:  # TODO Check
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, courses.id, courses.name,
                            users.id, users.name, users.surname FROM lessons
                            JOIN courses ON courses.id = lessons.course_id
                            LEFT JOIN teachers ON teachers.course_id = lessons.course_id
                            LEFT JOIN users ON users.id = teachers.user_id
                            WHERE NOT EXISTS (SELECT * FROM bookings WHERE bookings.status != 'CANCELED'
                            AND bookings.lesson_id = lessons.id AND bookings.teacher_id = teachers.user_id)
                            AND lessons.id = ?""", [lesson_id])
        db_data = cursor.fetchall()
        cursor.close()
    if db_data:
        teachers = []
        for key, rows in itertools.groupby(db_data, key=lambda x: x[3]):
            for r in rows:
                teachers.append({'id': r[5], 'name': r[6], 'surname': r[7]})
        lesson = {'id': db_data[0][0], 'unix_day': db_data[0][1], 'init_hour': db_data[0][2], 'teachers': teachers}
        return make_response({'ok': True, 'data': {'course': {'id': db_data[0][3], 'name': db_data[0][4]},
                                                   'lesson': lesson}}, 200)
    return make_response({'ok': False, 'error': 'LESSON_NOT_FOUND'}, 404)

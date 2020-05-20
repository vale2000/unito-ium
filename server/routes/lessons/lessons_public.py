#!/usr/bin/python3
import itertools

from flask import Blueprint, make_response

from modules.database import get_db_conn
from server_error import server_error

route_lessons_public = Blueprint('route_lessons_public', __name__)


# --------------------------
# Return lessons list
# --------------------------
@route_lessons_public.route('/lessons', methods=['GET'])
def lesson_list():
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, courses.id, courses.name FROM lessons
                            JOIN courses ON courses.id = lessons.course_id
                            LEFT JOIN teachers ON teachers.course_id = lessons.course_id
                            WHERE NOT EXISTS (SELECT * FROM bookings 
                            JOIN lessons AS book_lesson ON book_lesson.id = bookings.lesson_id
                            WHERE bookings.status != 'CANCELED'AND bookings.teacher_id = teachers.user_id AND
                            book_lesson.unix_day = lessons.unix_day AND book_lesson.init_hour = lessons.init_hour)
                            GROUP BY courses.name, lessons.unix_day, lessons.init_hour
                            ORDER BY courses.name, lessons.unix_day DESC, lessons.init_hour DESC""")
        db_data = cursor.fetchall()
        cursor.close()
    db_results = []
    if db_data:
        for row in db_data:
            course = {'id': row[3], 'name': row[4]}
            db_results.append({'id': row[0], 'unix_day': row[1], 'init_hour': row[2], 'course': course})
    return make_response({'ok': True, 'data': db_results}, 200)


# --------------------------
# Return lesson <id> data
# --------------------------
@route_lessons_public.route('/lessons/<int:lesson_id>', methods=['GET'])
def lesson_get(lesson_id: int):
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT lessons.id, lessons.unix_day, lessons.init_hour, courses.id, courses.name, users.id,
                            users.name, users.surname FROM lessons
                            JOIN courses ON courses.id = lessons.course_id
                            LEFT JOIN teachers ON teachers.course_id = lessons.course_id
                            LEFT JOIN users ON users.id = teachers.user_id
                            WHERE lessons.id = ? AND NOT EXISTS (SELECT * FROM bookings 
                            JOIN lessons AS book_lesson ON book_lesson.id = bookings.lesson_id
                            WHERE bookings.status != 'CANCELED'AND bookings.teacher_id = teachers.user_id AND
                            book_lesson.unix_day = lessons.unix_day AND book_lesson.init_hour = lessons.init_hour)""",
                       [lesson_id])
        db_data = cursor.fetchall()
        cursor.close()
    if db_data:
        teachers = []
        for key, rows in itertools.groupby(db_data, key=lambda x: x[3]):
            for r in rows:
                teachers.append({'id': r[5], 'name': r[6], 'surname': r[7]})
        course = {'id': db_data[0][3], 'name': db_data[0][4]}
        return make_response({'ok': True, 'data': {'id': db_data[0][0], 'unix_day': db_data[0][1],
                                                   'init_hour': db_data[0][2], 'course': course,
                                                   'available_teachers': teachers}}, 200)
    return server_error('LESSON_NOT_FOUND')

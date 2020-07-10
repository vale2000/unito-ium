#!/usr/bin/python3
import itertools

from flask import Blueprint, make_response

from modules.database import get_db_conn
from server_error import server_error

route_lessons = Blueprint('route_lessons', __name__)


# --------------------------
# Return lessons list
# --------------------------
@route_lessons.route('/lessons', methods=['GET'])
def lesson_list():
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""WITH week(day) AS (SELECT (strftime('%s', 'now', 'localtime', 'start of day') 
                            + weeksum.to_sum) FROM weeksum) SELECT courses.id, courses.name, COUNT(teachers.user_id), 
                            week.day FROM courses JOIN week ON true LEFT JOIN teachers ON 
                            teachers.course_id = courses.id WHERE NOT EXISTS (SELECT * FROM bookings WHERE 
                            bookings.status != 'CANCELED' AND bookings.teacher_id = teachers.user_id AND 
                            bookings.day = week.day GROUP BY bookings.day HAVING COUNT(bookings.hour) >= 5)
                            GROUP BY courses.name, week.day ORDER BY week.day, courses.name""")
        db_data = cursor.fetchall()
        cursor.close()
    db_results = []
    if db_data:
        for row in db_data:
            course = {'id': row[0], 'name': row[1]}
            db_results.append({'day': row[3], 'free_teachers': row[2], 'course': course})
    return make_response({'ok': True, 'data': db_results}, 200)


# -----------------------------
# Return lessons list by <day>
# -----------------------------
@route_lessons.route('/lessons/<int:day>', methods=['GET'])
def lesson_list_day(day: int):
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT courses.id, courses.name, COUNT(teachers.user_id) FROM courses
                            LEFT JOIN teachers ON teachers.course_id = courses.id 
                            WHERE NOT EXISTS (SELECT * FROM bookings WHERE bookings.status != 'CANCELED' 
                            AND bookings.teacher_id = teachers.user_id AND bookings.day = ?
                            GROUP BY bookings.day HAVING COUNT(bookings.hour) >= 5)
                            GROUP BY courses.name ORDER BY courses.name""", [day])
        db_data = cursor.fetchall()
        cursor.close()
    db_results = []
    if db_data:
        for row in db_data:
            course = {'id': row[0], 'name': row[1]}
            db_results.append({'day': day, 'free_teachers': row[2], 'course': course})
    return make_response({'ok': True, 'data': db_results}, 200)


# --------------------------
# Return lesson <id> data
# --------------------------
@route_lessons.route('/lessons/<int:day>/<int:course>', methods=['GET'])
def lesson_get(day: int, course: int):
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT courses.id, courses.name, users.id, users.name, users.surname, 
                            bookings.hour FROM courses
                            LEFT JOIN teachers ON teachers.course_id = courses.id
                            LEFT JOIN users ON users.id = teachers.user_id
                            LEFT JOIN bookings ON bookings.teacher_id = users.id AND bookings.day = ? 
                            AND bookings.status != 'CANCELED'
                            WHERE courses.id = ?
                            ORDER BY courses.id, users.id, bookings.day""", [day, course])
        db_data = cursor.fetchall()
        cursor.close()
    if db_data:
        teachers = []
        for key, rows in itertools.groupby(db_data, key=lambda x: x[2]):
            t = None
            available_on = [46800, 50400, 54000, 57600, 61200]
            for r in rows:
                if t is None:
                    t = r
                try:
                    available_on.remove(int(r[5]))
                except (ValueError, TypeError):
                    pass
            if len(available_on) > 0:
                teachers.append({'id': t[2], 'name': t[3], 'surname': t[4], 'available_on': available_on})

        course = {'id': db_data[0][0], 'name': db_data[0][1]}
        return make_response({'ok': True, 'data': {'day': day, 'course': course, 'teachers': teachers}}, 200)
    return server_error('LESSON_NOT_FOUND')

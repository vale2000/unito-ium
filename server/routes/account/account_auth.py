#!/usr/bin/python3
import itertools

from flask import Blueprint, request, make_response

from modules import simple_jwt
from modules.database import get_db_conn
from modules.utils import logged_before_request
from server_error import server_error

route_account_auth = Blueprint('route_account_auth', __name__)
route_account_auth.before_request(logged_before_request)  # Check for login


@route_account_auth.route('/account/profile', methods=['GET'])
def account_profile():
    token_data = simple_jwt.read(request.headers.get('Authorization').split(' ')[1])
    user_id = token_data.get('user')
    with get_db_conn(True) as database:
        cursor = database.cursor()
        cursor.execute("""SELECT users.id, users.email, users.name, users.surname, users.gender,
                            roles.id, roles.name, courses.id, courses.name FROM users
                            JOIN roles ON roles.id = users.role_id
                            LEFT JOIN teachers ON teachers.user_id = users.id
                            LEFT JOIN courses ON courses.id = teachers.course_id
                            WHERE users.id = ?
                            ORDER BY courses.name""", [user_id])
        db_data = cursor.fetchall()
        cursor.close()
    if db_data:
        user = {'id': db_data[0][0], 'email': db_data[0][1], 'name': db_data[0][2], 'surname': db_data[0][3],
                'gender': db_data[0][4], 'role': {'id': db_data[0][5], 'name': db_data[0][6]}}
        for key, rows in itertools.groupby(db_data, key=lambda x: x[0]):
            courses = []
            for r in rows:
                if r[7] is not None:
                    courses.append({'id': r[7], 'name': r[8]})
            user['courses'] = courses
        return make_response({'ok': True, 'data': user}, 200)
    return server_error('USER_ALREADY_EXIST')

#!/usr/bin/python3
from flask import make_response


def server_error(s: str):
    s = s.upper()
    string = server_error.__data.get(s, None)
    if string is None:
        string = server_error.__data.get('UNKNOWN_ERROR')
        return make_response({'ok': False, 'error': 'UNKNOWN_ERROR'}, string)
    return make_response({'ok': False, 'error': s}, string)


server_error.__data = {
    'USER_ALREADY_EXIST': 400,
    'BAD_REQUEST': 400,
    'UNAUTHORIZED': 401,
    'AUTH_FAILED': 401,
    'LOGIN_FAILED': 401,
    'NOT_FOUND': 404,
    'USER_NOT_FOUND': 404,
    'BOOKING_NOT_FOUND': 404,
    'LESSON_NOT_FOUND': 404,
    'COURSE_NOT_FOUND': 404,
    'METHOD_NOT_ALLOWED': 405,
    'LOGIN_DISABLED': 500,
    'SERVER_ERROR': 500,
    'UNKNOWN_ERROR': 204,
    'SERVER_OFFLINE': -1
}

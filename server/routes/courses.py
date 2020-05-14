#!/usr/bin/python3
from flask import Blueprint
route_courses = Blueprint('route_courses', __name__)


# --------------------------
# Return courses list
# --------------------------
@route_courses.route('/courses', methods=['GET'])
def course_list():
    pass


# --------------------------
# Create new course
# --------------------------
@route_courses.route('/courses', methods=['POST'])
def course_add():
    pass


# --------------------------
# Return course <id> data
# --------------------------
@route_courses.route('/courses/<int:id>', methods=['GET'])
def course_get(id: int):
    pass


# --------------------------
#
# --------------------------
@route_courses.route('/courses/<int:id>', methods=['PUT'])
def course_update(id: int):
    pass


# --------------------------
#
# --------------------------
@route_courses.route('/courses/<int:id>', methods=['DELETE'])
def course_remove(id: int):
    pass

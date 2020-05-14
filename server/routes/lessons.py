#!/usr/bin/python3
from flask import Blueprint
route_lessons = Blueprint('route_lessons', __name__)


# --------------------------
# Return lessons list
# --------------------------
@route_lessons.route('/lessons', methods=['GET'])
def lesson_list():
    pass


# --------------------------
# Create new lesson
# --------------------------
@route_lessons.route('/lessons', methods=['POST'])
def lesson_add():
    pass


# --------------------------
# Return lesson <id> data
# --------------------------
@route_lessons.route('/lessons/<int:id>', methods=['GET'])
def lesson_get(id: int):
    pass


# --------------------------
#
# --------------------------
@route_lessons.route('/lessons/<int:id>', methods=['PUT'])
def lesson_update(id: int):
    pass


# --------------------------
#
# --------------------------
@route_lessons.route('/lessons/<int:id>', methods=['DELETE'])
def lesson_remove(id: int):
    pass

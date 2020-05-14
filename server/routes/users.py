#!/usr/bin/python3
from flask import Blueprint
route_users = Blueprint('route_users', __name__)


# --------------------------
# Return users list
# --------------------------
@route_users.route('/users', methods=['GET'])
def user_list():
    pass


# --------------------------
# Create new user
# --------------------------
@route_users.route('/users', methods=['POST'])
def user_add():
    pass


# --------------------------
# Return user <id> data
# --------------------------
@route_users.route('/users/<int:id>', methods=['GET'])
def user_get(id: int):
    pass


# --------------------------
#
# --------------------------
@route_users.route('/users/<int:id>', methods=['PUT'])
def user_update(id: int):
    pass


# --------------------------
#
# --------------------------
@route_users.route('/users/<int:id>', methods=['DELETE'])
def user_remove(id: int):
    pass

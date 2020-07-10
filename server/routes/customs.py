#!/usr/bin/python3
from flask import Blueprint, make_response

from server_error import server_error

route_customs = Blueprint('route_customs', __name__)


# --------------------------
#
# --------------------------
@route_customs.route('/')
def home_page():
    return make_response({"ok": True, "data": "Hi!"}, 200)


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(400)
def not_found(err):
    """ Bad Request """
    return server_error('BAD_REQUEST')


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(401)
def not_found(err):
    """ Unauthorized """
    return server_error('UNAUTHORIZED')


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(404)
def not_found(err):
    """ Not Found """
    return server_error('NOT_FOUND')


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(405)
def not_found(err):
    """ Method Not Allowed """
    return server_error('METHOD_NOT_ALLOWED')


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(500)
def _server_error(err):
    """ Internal Server Error """
    return server_error('SERVER_ERROR')

#!/usr/bin/python3
from flask import Blueprint, make_response

route_customs = Blueprint('route_customs', __name__)


# --------------------------
#
# --------------------------
@route_customs.route('/')
def home_page():
    return make_response('{"ok": true, "data": "Hi!"}', 200)


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(400)
def not_found(err):
    """ Bad Request """
    return make_response('{"ok": false, "error": "BAD_REQUEST"}', 400)


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(401)
def not_found(err):
    """ Unauthorized """
    return make_response('{"ok": false, "error": "UNAUTHORIZED"}', 401)


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(404)
def not_found(err):
    """ Not Found """
    return make_response('{"ok": false, "error": "NOT_FOUND"}', 404)


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(405)
def not_found(err):
    """ Method Not Allowed """
    return make_response('{"ok": false, "error": "METHOD_NOT_ALLOWED"}', 405)


# --------------------------
#
# --------------------------
@route_customs.app_errorhandler(500)
def server_error(err):
    """ Internal Server Error """
    return make_response('{"ok": false, "error": "SERVER_ERROR"}', 500)

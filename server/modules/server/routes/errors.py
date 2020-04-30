from flask import Blueprint, make_response
errors = Blueprint('errors_routes', __name__)


@errors.app_errorhandler(400)
def not_found(err):
    """ Bad Request """
    return make_response('{"ok": false, "error": "BAD_REQUEST"}')


@errors.app_errorhandler(401)
def not_found(err):
    """ Unauthorized """
    return make_response('{"ok": false, "error": "UNAUTHORIZED"}')


@errors.app_errorhandler(404)
def not_found(err):
    """ Not Found """
    return make_response('{"ok": false, "error": "NOT_FOUND"}')


@errors.app_errorhandler(405)
def not_found(err):
    """ Method Not Allowed """
    return make_response('{"ok": false, "error": "METHOD_NOT_ALLOWED"}')


@errors.app_errorhandler(500)
def server_error(err):
    """ Internal Server Error """
    return make_response('{"ok": false, "error": "SERVER_ERROR"}')

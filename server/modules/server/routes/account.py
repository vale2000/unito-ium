import time
from flask import Blueprint, make_response, request, abort

from modules.config import Config
from modules.database import Database
from modules.simplejwt import SimpleJWT

account = Blueprint('account', __name__)


@account.route('/account', methods=['POST'])
def post():
    if not (request.json and request.json.get('cmd') and request.json.get('data')):
        return abort(400)
    cmd = request.json.get('cmd')
    email = request.json.get('data').get('email')
    psswd = request.json.get('data').get('password')
    if not (email and psswd):
        return abort(400)
    if cmd == 'login':
        return login(email, psswd)
    elif cmd == 'register':
        return register(email, psswd)
    else:
        return make_response('{"ok": false, "error": "CMD_NOT_FOUND"}', 404)


def login(email: str, password: str):
    user = Database.get_instance().get_user_login(email, password)
    if not user:
        return make_response('{"ok": false, "error": "USER_NOT_FOUND"}', 404)
    user['utime'] = int(time.time())
    sjwt = SimpleJWT(Config.get_instance().get('security', 'secret'))
    json = '{"ok": true, "data": {"token": "%s", "utime": %d}}' % (sjwt.generate(user), user.get('utime'))
    return make_response(json, 200)


def register(email: str, password: str, name: str = None, surname: str = None):
    user_add = Database.get_instance().add_user(email, password, name, surname)
    if not user_add:
        return abort(500)
    return make_response('{"ok": true, "data": {}}', 200)


@account.route('/account', methods=['DELETE'])
def delete():
    return make_response('{"ok": false, "error": "NOT_IMPL"}', 500)

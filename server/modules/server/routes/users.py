from flask import Blueprint, make_response, request, abort
import json
from modules.config import Config
from modules.database import Database
from modules.simplejwt import SimpleJWT

users = Blueprint('users', __name__)


@users.before_request
def users_before():
    token = request.headers.get('Authorization').split(' ')
    if not token or token[0].lower() != 'bearer':
        return abort(401)
    sjwt = SimpleJWT(Config.get_instance().get('security', 'secret'))
    if not sjwt.check(token[1]):
        return abort(401)


@users.route('/users/<int:user_id>', methods=['GET'])
def user_get(user_id: int):
    token = SimpleJWT.read(request.headers.get('Authorization').split(' ')[1])
    if token.get('is_teacher') == 0 and token.get('id') != user_id:
        return abort(401)
    user = Database.get_instance().get_user(user_id)
    if user:
        jsn = json.dumps({'ok': True, 'data': [user]})
    else:
        jsn = '{"ok": True, "data": []}'
    return make_response(jsn, 200)


@users.route('/users', methods=['GET'])
def users_get():
    token = SimpleJWT.read(request.headers.get('Authorization').split(' ')[1])
    if token.get('is_teacher') == 0:
        return abort(400)
    users = Database.get_instance().get_users()
    jsn = json.dumps({'ok': True, 'data': users})
    return make_response(jsn, 200)

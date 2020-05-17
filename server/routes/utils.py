#!/usr/bin/python3
from flask import request, abort
from modules import simple_jwt


# ----------------------------------------
# Check Auth Token before execute request
# ----------------------------------------
def logged_before_request():
    auth = request.headers.get('Authorization')
    if auth is not None:
        token = auth.split(' ')
        if token[0].lower() == 'bearer':
            token_check = simple_jwt.check(token[1])
            if token_check:
                return
    abort(401)


# -------------------------
# Return correct "user_id"
# -------------------------
def get_user_id(token_data, req_data):
    user_id = token_data.get('user')
    if token_data.get('role') == 3:
        if req_data:
            user_id = req_data.get('user_id', token_data.get('user'))  # Use POST data "user_id" or fallback
    return user_id

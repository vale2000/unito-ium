#!/usr/bin/python3
from flask import Blueprint
route_account = Blueprint('route_account', __name__)


@route_account.route('/account/login', methods=['POST'])
def account_login():
    pass


@route_account.route('/account/register', methods=['POST'])
def account_register():
    pass

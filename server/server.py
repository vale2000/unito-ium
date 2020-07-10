#!/usr/bin/python3

from flask import Flask

from modules import config
from modules.database import init_db
from routes import route_users, route_customs, route_bookings, route_lessons
from routes.account import route_account_public, route_account_auth
from routes.courses import route_courses_auth, route_courses_public

# ---------------------
# Flask initialization
# ---------------------
app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False
app.config['JSONIFY_PRETTYPRINT_REGULAR'] = False
app.register_blueprint(route_customs)
app.register_blueprint(route_account_auth)
app.register_blueprint(route_account_public)
app.register_blueprint(route_courses_auth)
app.register_blueprint(route_courses_public)
app.register_blueprint(route_users)
app.register_blueprint(route_lessons)
app.register_blueprint(route_bookings)


# ------------------------
# Set Flask after request
# ------------------------
@app.after_request
def after_request(response):
    response.headers.set('Access-Control-Allow-Origin', '*')
    response.headers.set('Content-Type', 'application/json')
    return response


# -------------------
# Start Flask server
# -------------------
if __name__ == '__main__':
    init_db()
    app.run(config.get('server', 'address'), config.getint('server', 'port'), debug=True)

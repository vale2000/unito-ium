#!/usr/bin/python3
from flask import Flask
from routes import route_users, route_customs, route_account, route_bookings
from routes.lessons import route_lessons_auth, route_lessons_public
from routes.courses import route_courses_auth, route_courses_public
from modules import config
from modules.database import init_db


# ---------------------
# Flask initialization
# ---------------------
app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False
app.register_blueprint(route_customs)
app.register_blueprint(route_account)
app.register_blueprint(route_users)
app.register_blueprint(route_bookings)
app.register_blueprint(route_lessons_auth)
app.register_blueprint(route_lessons_public)
app.register_blueprint(route_courses_auth)
app.register_blueprint(route_courses_public)


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
    app.run(config.get('server', 'address'), config.getint('server', 'port'))
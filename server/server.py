#!/usr/bin/python3
from flask import Flask
from routes import route_users, route_customs, route_account, route_bookings, route_lessons, route_courses
from modules import config


# ---------------------
# Flask initialization
# ---------------------
app = Flask(__name__)
app.register_blueprint(route_customs)
app.register_blueprint(route_account)
app.register_blueprint(route_users)
app.register_blueprint(route_bookings)
app.register_blueprint(route_lessons)
app.register_blueprint(route_courses)


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
    app.run(config.get('server', 'address'), config.getint('server', 'port'))

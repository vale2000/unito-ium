#!/usr/bin/python3
from flask import Flask
from routes import customs, account, users, bookings, lessons, courses

# ----------------------------
# Flask Server initialization
# ----------------------------
app = Flask(__name__)
app.register_blueprint(customs.route_customs)
app.register_blueprint(account.route_account)
app.register_blueprint(users.route_users)
app.register_blueprint(bookings.route_bookings)
app.register_blueprint(lessons.route_lessons)
app.register_blueprint(courses.route_courses)


# ------------------------------------
# Set JSON Headers for every response
# ------------------------------------
@app.after_request
def after_request(response):
    response.headers.set('Access-Control-Allow-Origin', '*')
    response.headers.set('Content-Type', 'application/json')
    return response


if __name__ == '__main__':
    app.run()

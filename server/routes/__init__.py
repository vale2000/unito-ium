#!/usr/bin/python3
from routes import customs, users, bookings, lessons

# -----------------------------------------
# Shortcut for Flask BluePrints inside .py
# -----------------------------------------
route_customs = customs.route_customs
route_users = users.route_users
route_bookings = bookings.route_bookings
route_lessons = lessons.route_lessons

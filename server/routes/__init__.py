#!/usr/bin/python3
from routes import customs, account, users, bookings


# -----------------------------------------
# Shortcut for Flask BluePrints inside .py
# -----------------------------------------
route_customs = customs.route_customs
route_account = account.route_account
route_users = users.route_users
route_bookings = bookings.route_bookings

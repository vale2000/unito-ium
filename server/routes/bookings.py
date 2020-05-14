#!/usr/bin/python3
from flask import Blueprint
route_bookings = Blueprint('route_bookings', __name__)


# --------------------------
# Return bookings list
# --------------------------
@route_bookings.route('/bookings', methods=['GET'])
def booking_list():
    pass


# --------------------------
# Create new booking
# --------------------------
@route_bookings.route('/bookings', methods=['POST'])
def booking_add():
    pass


# --------------------------
# Return booking <id> data
# --------------------------
@route_bookings.route('/bookings/<int:id>', methods=['GET'])
def booking_get(id: int):
    pass


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:id>', methods=['PUT'])
def booking_update(id: int):
    pass


# --------------------------
#
# --------------------------
@route_bookings.route('/bookings/<int:id>', methods=['DELETE'])
def booking_remove(id: int):
    pass

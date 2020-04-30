#!/usr/bin/python3
import json
import socket
from http import HTTPStatus
from http.server import BaseHTTPRequestHandler
from modules.webserver import Handler


# ------------------------
# WebServer Class
# ------------------------
class QuiteSimpleWebServer(BaseHTTPRequestHandler):
    METHODS = {
        'GET': 0x01,
        'PUT': 0x02,
        'POST': 0x04,
        'DELETE': 0x08,
        'PATCH': 0x10,
        'WILDCARD': 0xFF
    }

    def __init__(self):
        pass

    def __path_segments(self):
        return self.path.strip('/').split('/')

    def add_handler(self, route str, handler: Handler):

























    routes = {
        '/account': Account,  # Catch "server.com/account"
        '/users': Users,  # Catch "server.com/users"    & "server.com/users/*int*"
        '/courses': Courses,  # Catch "server.com/courses"  & "server.com/courses/*int*"
        '/lessons': Lessons,  # Catch "server.com/lessons"  & "server.com/lessons/*int*"
        '/bookings': Bookings  # Catch "server.com/bookings" & "server.com/bookings/*int*"
    }

    def handle_one_request(self):
        try:
            if self.__read_raw_request_line():
                segments = WebServer.__path_segments(self.path)
                route_class = WebServer.routes.get('/' + segments[1])
                if not route_class:
                    self.__handler_not_found()
                    return

                route = route_class(self.command, self.headers)

                if len(segments) == 3 and segments[2]:
                    route.set_request_id(int(segments[2]))

                route.set_post_data(self.__read_post_data())
                route.handle()
                self.__send_headers(route.get_response_code())
                self.__send_content(route.get_response_text())
        except socket.timeout as e:
            self.log_error("Request timed out: %r", e)
            self.close_connection = True

    def __handler_not_found(self):
        self.__send_headers(404)
        self.__send_content('{"ok": false, "error": "NOT_FOUND"}')

    def __read_post_data(self):
        try:
            content_length = int(self.headers.get('Content-Length')) if self.headers.get('Content-Length') else None
            if content_length:
                req_body = self.rfile.read(content_length)
                return json.loads(req_body)
        except json.decoder.JSONDecodeError:
            print('__read_post_data: ERROR')
        return None

    def __send_content(self, data: str):
        self.wfile.write(data.encode())
        self.wfile.flush()

    def __send_headers(self, code: int):
        self.send_response(code)
        self.send_header('Content-Type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()

    def __read_raw_request_line(self):
        self.raw_requestline = self.rfile.readline(65537)
        if len(self.raw_requestline) > 65536:
            self.requestline = ''
            self.request_version = ''
            self.command = ''
            self.send_error(HTTPStatus.REQUEST_URI_TOO_LONG)
            return False
        if not self.raw_requestline:
            self.close_connection = True
            return False
        if not self.parse_request():
            return False  # An error code has been sent, just exit
        return True

    @staticmethod
    def __path_segments(path: str):
        return path.split('/')

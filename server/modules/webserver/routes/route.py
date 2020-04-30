#!/usr/bin/python3
from modules.config import Config
from modules.simplejwt import SimpleJWT
from modules.database import Database
import json


# ------------------------
# WebServer Class
# ------------------------
class Route:
    def __init__(self, method: str, headers: dict):
        self.post_data = None
        self.request_id = None
        self.method = method
        self.headers = headers
        self.__response_code = 501
        self.__response_text = '{"ok": false, "error": "NOT_IMPL"}'
        self.db = Database.get_instance()

    def handle(self):
        mname = 'do_' + self.method
        if hasattr(self, mname):
            handler = getattr(self, mname)
            self.set_response_code(200)
            self.__response_text = json.dumps(handler())

    def set_database(self, db: Database):
        self.db = db

    def set_post_data(self, post_data: dict):
        self.post_data = post_data

    def set_request_id(self, req_id: int):
        self.request_id = req_id

    def set_response_code(self, code: int):
        self.__response_code = code

    def get_response_code(self):
        return self.__response_code

    def get_response_text(self):
        return self.__response_text

    def check_login(self):
        auth = self.headers.get('Authorization')
        if auth:
            auth_segs = auth.split(' ')
            if auth_segs or auth_segs[0] != 'Bearer':
                jwt = SimpleJWT(Config.get_instance().get('security', 'secret'))
                return jwt.check_and_read(auth_segs[1])
        return False

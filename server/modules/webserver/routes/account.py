#!/usr/bin/python3
import time
from modules.config import Config
from modules.simplejwt import SimpleJWT
from modules.webserver.routes.route import Route


# ------------------------
# Users Route Class
# ------------------------
class Account(Route):
    def do_POST(self):
        if not self.post_data or not self.post_data.get('cmd') or not self.post_data.get('data'):
            self.set_response_code(400)
            return {"ok": False, "error": "MISSING_PARAMS"}
        if self.post_data.get('cmd') == 'register':
            return self.__register_user(self.post_data.get('data'))
        elif self.post_data.get('cmd') == 'login':
            return self.__login_user(self.post_data.get('data'))
        else:
            self.set_response_code(400)
            return {"ok": False, "error": "CMD_UNKNOWN"}

    def __register_user(self, data: dict):
        email = data.get('email')
        psswd = data.get('password')
        fname = data.get('name')
        sname = data.get('surname')
        if not email or not psswd:
            self.set_response_code(400)
            return {"ok": False, "error": "MISSING_PARAMS"}
        if not self.db.add_user(email, psswd, fname, sname):
            self.set_response_code(500)
            return {"ok": False, "error": "SRV_ERR"}
        return {"ok": True, "result": {}}

    def __login_user(self, data: dict):
        email = data.get('email')
        psswd = data.get('password')
        if not email or not psswd:
            self.set_response_code(400)
            return {"ok": False, "error": "MISSING_PARAMS"}
        db_user = self.db.get_user_login(email, psswd)
        if not db_user:
            self.set_response_code(403)
            return {"ok": False, "error": "USR_NOT_FOUND"}
        db_user['utime'] = int(time.time())
        token = SimpleJWT(Config.get_instance().get('security', 'secret')).generate(db_user)
        print(db_user)
        return {"ok": True, "result": {"token": token, "utime": db_user.get('utime')}}


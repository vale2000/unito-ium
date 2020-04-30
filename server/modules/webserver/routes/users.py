#!/usr/bin/python3
from modules.webserver.routes.route import Route


# ------------------------
# Users Route Class
# ------------------------
class Users(Route):
    def do_GET(self):
        login = self.check_login()
        if login:
            if login.get('is_teacher') == 1:
                if self.request_id:
                    return {"ok": True, "result": self.db.get_user(self.request_id)}
                else:
                    return {"ok": True, "result": self.db.get_users()}
            elif login.get('id'):
                if not self.request_id or self.request_id == login.get('id'):
                    return {"ok": True, "result": self.db.get_user(login.get('id'))}
                else:
                    self.set_response_code(401)
                    return {"ok": False, "error": "PERM_DENIED"}
        self.set_response_code(401)
        return {"ok": False, "error": "ACC_REQUIRED"}

    def do_POST(self):
        pass

    def do_PUT(self):
        pass

    def do_DELETE(self):
        pass


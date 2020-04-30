#!/usr/bin/python3
from modules.webserver.routes.route import Route


# ------------------------
# Courses Route Class
# ------------------------
class Courses(Route):
    def do_GET(self):
        if self.request_id:
            return {"ok": True, "result": self.db.get_course(self.request_id)}
        return {"ok": True, "result": self.db.get_courses()}

    def do_POST(self):
        pass

    def do_PUT(self):
        pass

    def do_DELETE(self):
        pass
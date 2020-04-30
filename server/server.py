#!/usr/bin/python3
from http.server import HTTPServer
from modules.config import Config
from modules.database import Database
from modules.webserver.webserver import WebServer
import signal
import sys


# ------------------------
# Server Main Class
# ------------------------
class Server:
    def __init__(self):
        self.__init_config('config.ini')
        self.__init_db()
        self.__init_server()
        signal.signal(signal.SIGINT, self.__sigint_handler)

    def __init_config(self, conf_path: str):
        self.config = Config.get_instance(conf_path)

    def __init_db(self):
        if self.config:
            self.db = Database.get_instance(
                self.config.get('database', 'db_path'),
                self.config.get('database', 'dump_path'))
        else:
            raise Exception('Config class not initialized!')

    def __init_server(self):
        if self.config and self.db:
            address = (self.config.get('server', 'address'), self.config.getint('server', 'port'))
            self.server = HTTPServer(address, WebServer)

    def start(self):
        if self.server:
            self.server.serve_forever()

    def __sigint_handler(self, signum, frame):
        print()
        self.db.close()
        self.server.server_close()
        sys.exit(0)


if __name__ == '__main__':
    httpd = Server()
    httpd.start()

#!/usr/bin/python3
from http.server import HTTPServer, BaseHTTPRequestHandler
from modules.config import Config
from modules.database import Database
import signal
import sys


# ------------------------
# Api WebServer
# ------------------------
class ApiServer:
    def __init__(self, db_instance: Database):
        self.db = db_instance

    def start(self):
        pass

    def close(self):
        print('Api Server closed.')


# ------------------------
if __name__ == '__main__':
    config = Config('config.ini')

    dbInstance = Database(config.get('database', 'filepath'))
    dbInstance.add_user('pino@gmail.com', 'SHA256PWD')
    print(dbInstance.is_teacher(1))

    apiServer = ApiServer(dbInstance)
    apiServer.start()


# SIGINT Handler
def signal_handler():
    print()
    apiServer.close()
    dbInstance.close()
    sys.exit(0)


signal.signal(signal.SIGINT, signal_handler)

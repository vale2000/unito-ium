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
    dbInstance = Database(config.get('database', 'db_path'), config.get('database', 'dump_path'))
    server_address = (config.get('server', 'address'), config.getint('server', 'port'))


# SIGINT Handler
def signal_handler():
    print()
    dbInstance.close()
    sys.exit(0)


signal.signal(signal.SIGINT, signal_handler)

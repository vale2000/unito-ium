#!/usr/bin/python3
from http.server import HTTPServer, BaseHTTPRequestHandler
from database import Database
import signal
import sys

# ------------------------
# Api WebServer
# ------------------------
class ApiServer():
  def __init__(self, dbInstance):
    self.db = dbInstance

  def start(self):
    pass

  def close(self):
    print("Api Server closed.")

# ------------------------
if __name__ == '__main__':
  dbInstance = Database('./database.sqlite')
  apiServer = ApiServer(dbInstance)
  apiServer.start()

# SIGINT Handler
def signal_handler(sig, frame):
    apiServer.close()
    dbInstance.close()
    sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)
signal.pause()
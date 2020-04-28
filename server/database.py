#!/usr/bin/python3
import sqlite3

# ------------------------
# SQLite3 Database
# ------------------------
class Database():
  def __init__(self, dbFile):
    try:
      self.conn = sqlite3.connect(dbFile)
    except sqlite3.Error as err:      
      self.conn = False
      exit(err)

  def close(self):
    self.conn.close()
    print("Database closed.")

  def execSql(self, sql):
    if self.conn is not False:
      try:
        c = self.conn.cursor()
        c.execute(sql)
        return True
      except sqlite3.Error as err:
        print(err)
    return False

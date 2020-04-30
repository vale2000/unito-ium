#!/usr/bin/python3
from modules.config import Config
from modules.database import Database
from modules.server.QuiteSimpleWs import QuiteSimpleWs

from modules.server.routes.account import account
from modules.server.routes.errors import errors
from modules.server.routes.users import users

if __name__ == '__main__':
    cfInst = Config.get_instance('config.ini')
    dbInst = Database.get_instance(cfInst.get('database', 'path'), cfInst.get('database', 'dump'))
    qsws = QuiteSimpleWs(__name__, cfInst.get('server', 'address'), cfInst.getint('server', 'port'))
    qsws.add_blueprint(account)
    qsws.add_blueprint(users)
    qsws.add_blueprint(errors)
    qsws.run()

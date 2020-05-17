from modules.config import Config
from modules.database import Database
from modules.simplejwt import SimpleJWT

config = Config('config.ini')
database = Database(config.get('database', 'path'), config.get('database', 'dump'))
simple_jwt = SimpleJWT(config.get('JWT', 'secret'))
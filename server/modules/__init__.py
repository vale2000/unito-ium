from modules.config import Config
from modules.simplejwt import SimpleJWT

config = Config('config.ini')
simple_jwt = SimpleJWT(config.get('JWT', 'secret'))

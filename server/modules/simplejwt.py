#!/usr/bin/python3
import binascii
import hashlib
import base64
import hmac
import json


# ------------------------
# SimpleJWT Class
# ------------------------
class SimpleJWT:
    def __init__(self, secret: str):
        self.secret = secret

    def generate(self, data: dict):
        tkn_data = base64.b64encode(json.dumps(data).encode('utf-8'))  # .decode('utf-8')
        tkn_sign = SimpleJWT.__gen_sha256_sign(self.secret, tkn_data)
        tkn_str = '{}.{}'.format(tkn_data.decode('utf-8'), tkn_sign)
        return base64.b64encode(tkn_str.encode('ascii')).decode('ascii')

    def check_and_read(self, b64_token: str):
        try:
            tkn_str = base64.b64decode(b64_token.encode('ascii')).decode('ascii')
            tkn_segs = tkn_str.split('.')
            tkn_sign = SimpleJWT.__gen_sha256_sign(self.secret, tkn_segs[0])
            if tkn_sign == tkn_segs[1]:
                return json.loads(base64.b64decode(tkn_segs[0].encode('utf-8')).decode('utf-8'))
        except binascii.Error:
            pass
        return False

    @staticmethod
    def __gen_sha256_sign(key: str, token):
        key_bytes = key.encode('ascii')
        tkn_bytes = token.encode() if isinstance(token, str) else token
        return hmac.new(key_bytes, tkn_bytes, hashlib.sha256).hexdigest().upper()

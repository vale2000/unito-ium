#!/usr/bin/python3
import configparser


# ------------------------
# Config Class
# ------------------------
class Config:
    # Singleton Method
    __instance = None

    @staticmethod
    def get_instance(conf_file: str = None):
        if not Config.__instance and conf_file:
            Config.__instance = Config(conf_file)
        return Config.__instance

    # ------------------------
    # Class Methods
    def __init__(self, conf_file):
        with open(conf_file, 'r') as conf:
            self.conf = configparser.RawConfigParser(allow_no_value=True)
            self.conf.read_file(conf)

    def get(self, section, variable):
        try:
            return self.conf.get(section, variable)
        except (ValueError, configparser.Error):
            return ''

    def getint(self, section, variable):
        try:
            return self.conf.getint(section, variable)
        except (ValueError, configparser.Error):
            return -1

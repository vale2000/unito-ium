#!/usr/bin/python3
import configparser


# ------------------------
# Config Class
# ------------------------
class Config:
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
